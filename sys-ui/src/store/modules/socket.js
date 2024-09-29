import SocketIO from "socket.io-client";
import {Notification} from 'element-ui'
import cache from "@/plugins/cache";

const sockets = {

  state: {
    socket: null,
  },

  mutations: {
    SET_SOCKET: (state, socket) => {
      state.socket = socket;
    }
  },

  actions: {
    OpenSocket({ state, commit }, xx) {
      if(!cache.local.getUserId() || !cache.local.getAccessToken()){
        return;
      }
      if (state.socket) {
        return;
      }
      let socket;
      if (process.env.NODE_ENV === 'production') {
        socket = SocketIO('ws://' + location.host, {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        socket = SocketIO('ws://localhost:19011', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }
      commit('SET_SOCKET', socket);
      // 系统消息
      socket.on('sys_notice', (data) => {
        Notification.warning('系统消息：' + data)
      });

      // 流程消息
      socket.on('flow_notice', (data) => {
        Notification.warning(data)
      });

      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('socket connect error:', error);
      });
      socket.on('error', (error) => {
        console.error('socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('socket connect success');
      });
      socket.on('connect_failed', (error) => {
        console.log('socket connect failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('socket disconnect');
      });
      socket.open();
    },

    RefreshSocket({ state, commit }, xx) {
      if (state.socket) {
        state.socket.close();
      }
      let socket;
      if (process.env.NODE_ENV === 'production') {
        socket = SocketIO('ws://' + location.host, {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }else{
        socket = SocketIO('ws://localhost:19011', {
          autoConnect: true,
          path: '/socket.io',
          transports: ['websocket'],
          query: {
            clientId: cache.local.getUserCode(),
            Authorization: cache.local.getAccessToken()
          },
        });
      }
      commit('SET_SOCKET', socket);
      // 系统消息
      socket.on('sys_notice', (data) => {
        Notification.warning('系统消息：' + data)
      });

      // 流程消息
      socket.on('flow_notice', (data) => {
        Notification.warning(data)
      });

      // 连接事件
      socket.on('connect_error', (error) => {
        console.error('socket connect error:', error);
      });
      socket.on('error', (error) => {
        console.error('socket error:', error);
      });
      socket.on('connecting', (error) => {
        console.log('socket connecting');
      });
      socket.on('connect', (error) => {
        console.log('socket connect success');
      });
      socket.on('connect_failed', (error) => {
        console.log('socket connect failed', error);
      });
      socket.on('disconnect', (error) => {
        console.log('socket disconnect');
      });
      socket.open();
    },

    CloseSocket({ state }) {
      if (state.socket) {
        state.socket.close();
      }
    },
  }
}

export default sockets
