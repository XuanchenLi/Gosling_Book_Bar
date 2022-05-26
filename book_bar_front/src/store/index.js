import { createStore } from 'vuex'

export default createStore({
  state: {
    token: ''
  },
  getters: {
    get_token(state){
      return state.token || storage.get("token") || null;
    }
  },
  mutations: {
    set_token(state, token) {
      state.token = token
      sessionStorage.token = token
    },
    del_token(state) {
      state.token = ''
      sessionStorage.removeItem('token')
    }
  },
  actions: {
  },
  modules: {
  }
})
