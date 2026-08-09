// Substitui a busca no node_modules por uma CDN que entrega o pacote pronto para o navegador
import { Room, RoomEvent } from 'https://esm.sh/livekit-client';

// Configurações de Portas
const API_BASE = 'http://192.168.15.7:25565';
const LIVEKIT_URL = 'ws://192.168.15.7:8000';

const state = {
    token: '',
    currentUser: null,
    channels: [],
    messages: {},
    activeChannelId: null,
    stompClient: null,
    livekitRoom: null
};

// --- DOM Elements ---
const dom = {
    jwtToken: document.getElementById('jwtToken'),
    connectBtn: document.getElementById('connectBtn'),
    disconnectBtn: document.getElementById('disconnectBtn'),
    userInfo: document.getElementById('userInfo'),
    channelList: document.getElementById('channelList'),
    chatHeaderTitle: document.getElementById('chatHeaderTitle'),
    joinCallBtn: document.getElementById('joinCallBtn'),
    leaveCallBtn: document.getElementById('leaveCallBtn'),
    videoGrid: document.getElementById('videoGrid'),
    messagesContainer: document.getElementById('messagesContainer'),
    messageInput: document.getElementById('messageInput'),
    sendBtn: document.getElementById('sendBtn')
};

// --- INICIALIZAÇÃO DA APLICAÇÃO ---
dom.connectBtn.addEventListener('click', async () => {
    state.token = dom.jwtToken.value.trim();
    if (!state.token) return alert("Insira o token!");

    try {
        // Busca Usuário
        const meRes = await fetch(`${API_BASE}/user/me`, { headers: { 'Authorization': `Bearer ${state.token}` } });
        if (!meRes.ok) throw new Error("Falha na autenticação JWT");
        state.currentUser = (await meRes.json()).data;
        dom.userInfo.innerText = `Logado como: @${state.currentUser.username}`;

        // Busca Canais
        const channelsRes = await fetch(`${API_BASE}/channels/@me`, { headers: { 'Authorization': `Bearer ${state.token}` } });
        state.channels = (await channelsRes.json()).data || [];
        state.channels.forEach(ch => { state.messages[ch.id] = []; });

        renderChannelList();
        connectStompChat();

        dom.connectBtn.disabled = true;
        dom.disconnectBtn.disabled = false;
        dom.jwtToken.disabled = true;
    } catch (error) {
        alert(error.message);
    }
});

// --- CHAT EM TEXTO (STOMP - PORTA 25565) ---
function connectStompChat() {
    const socket = new SockJS(`${API_BASE}/ws`);
    state.stompClient = Stomp.over(socket);
    state.stompClient.debug = null;

    state.stompClient.connect({ 'Authorization': `Bearer ${state.token}` }, () => {
        state.channels.forEach(channel => {
            state.stompClient.subscribe(`/topic/channel.${channel.id}`, (message) => {
                const chatOutput = JSON.parse(message.body);
                state.messages[channel.id].push(chatOutput);
                if (state.activeChannelId === channel.id) renderMessages();
            });
        });
    });
}

dom.sendBtn.addEventListener('click', () => {
    const content = dom.messageInput.value.trim();
    if (!content || !state.activeChannelId) return;

    state.stompClient.send("/app/chat.send", {}, JSON.stringify({ channelId: state.activeChannelId, content }));
    dom.messageInput.value = '';
});

dom.messageInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') dom.sendBtn.click();
});

// --- RENDERIZAÇÃO DA INTERFACE ---
function renderChannelList() {
    dom.channelList.innerHTML = '';
    state.channels.forEach(channel => {
        const otherMember = channel.members.find(m => m.id !== state.currentUser.id);
        const name = otherMember ? `@${otherMember.username}` : 'Chat Desconhecido';

        const div = document.createElement('div');
        div.className = `channel-item ${state.activeChannelId === channel.id ? 'active' : ''}`;
        div.innerText = name;
        div.onclick = () => selectChannel(channel.id, name);
        dom.channelList.appendChild(div);
    });
}

function selectChannel(channelId, name) {
    state.activeChannelId = channelId;
    dom.chatHeaderTitle.innerText = `Conversando com: ${name}`;
    dom.messageInput.disabled = false;
    dom.sendBtn.disabled = false;

    // Mostra o botão de iniciar chamada (apenas se não estivermos já numa call)
    if (!state.livekitRoom || state.livekitRoom.state === 'disconnected') {
        dom.joinCallBtn.style.display = 'block';
        dom.leaveCallBtn.style.display = 'none';
    }

    renderChannelList();
    renderMessages();
}

function renderMessages() {
    dom.messagesContainer.innerHTML = '';
    const msgs = state.messages[state.activeChannelId] || [];
    const activeData = state.channels.find(c => c.id === state.activeChannelId);

    msgs.forEach(msg => {
        const isMe = msg.senderId === state.currentUser.id;
        const sender = activeData.members.find(m => m.id === msg.senderId);

        const row = document.createElement('div');
        row.className = `message-row ${isMe ? 'me' : 'them'}`;
        row.innerHTML = `
            <div class="message-sender">${sender ? sender.username : 'Desconhecido'}</div>
            <div class="message-bubble ${isMe ? 'me' : 'them'}">${msg.content}</div>
        `;
        dom.messagesContainer.appendChild(row);
    });
    dom.messagesContainer.scrollTop = dom.messagesContainer.scrollHeight;
}

// --- INTEGRAÇÃO WEBRTC (LIVEKIT - PORTA 8000) ---
dom.joinCallBtn.addEventListener('click', async () => {
    try {
        // 1. Solicitar o JWT de acesso da sala ao Backend (Spring Boot)
        const res = await fetch(`${API_BASE}/calls/${state.activeChannelId}/token`, {
            headers: { 'Authorization': `Bearer ${state.token}` }
        });

        if (!res.ok) throw new Error("Você não tem permissão para iniciar chamada aqui.");
        const jsonResponse = await res.json();

        const token = jsonResponse.data.token;

        // 2. Conectar na Sala do LiveKit
        const room = new Room();
        state.livekitRoom = room;

        // Ouvinte: Quando outra pessoa ligar a câmera/microfone
        room.on(RoomEvent.TrackSubscribed, (track, publication, participant) => {
            attachTrackToGrid(track, participant.identity);
        });

        // Ouvinte: Quando outra pessoa desligar
        room.on(RoomEvent.TrackUnsubscribed, (track, publication, participant) => {
            track.detach();
            const el = document.getElementById(`track-${track.sid}`);
            if (el) el.parentElement.remove();
        });

        await room.connect(LIVEKIT_URL, token);
        console.log("Conectado ao LiveKit SFU!");

        // 3. Atualizar UI
        dom.videoGrid.classList.add('active');
        dom.joinCallBtn.style.display = 'none';
        dom.leaveCallBtn.style.display = 'block';

        // 4. Ligar a nossa própria câmera e microfone e enviar para o SFU
        await room.localParticipant.setCameraEnabled(true);
        await room.localParticipant.setMicrophoneEnabled(true);

        // Anexar nosso vídeo local na tela
        room.localParticipant.videoTrackPublications.forEach((pub) => {
            if (pub.track) attachTrackToGrid(pub.track, state.currentUser.username, true);
        });

    } catch (err) {
        console.error("Erro no LiveKit:", err);
        alert(err.message);
    }
});

dom.leaveCallBtn.addEventListener('click', () => {
    if (state.livekitRoom) {
        state.livekitRoom.disconnect();
        state.livekitRoom = null;
    }

    dom.videoGrid.innerHTML = '';
    dom.videoGrid.classList.remove('active');
    dom.leaveCallBtn.style.display = 'none';
    dom.joinCallBtn.style.display = 'block';
});

// Helper para injetar o vídeo na Grid com o nome do usuário
function attachTrackToGrid(track, labelText, isLocal = false) {
    const element = track.attach();
    element.id = `track-${track.sid}`;

    if (isLocal) {
        element.muted = true; // Impede que você ouça a si mesmo
    }

    const container = document.createElement('div');
    container.className = 'video-container';

    const label = document.createElement('div');
    label.className = 'video-label';
    label.innerText = labelText;

    container.appendChild(element);
    container.appendChild(label);
    dom.videoGrid.appendChild(container);
}