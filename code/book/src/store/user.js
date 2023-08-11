// user.js文件
const state = {
    username: '', // 用户名
    role: '' // role
  }
  
  const mutations = {
    setUserData(state, { username, role }) {
      state.username = username
      state.role = role
    }
  }
  
  const getters = {
    username: state => state.username,
    role: state => state.role
  }
  
  const actions = {
    fetchUserData(context) {
      // 异步操作...
      context.commit('setUserData', { username: '', role: '' })
    }
  }
  
  export default {
    namespaced: true,
    state,
    mutations,
    getters,
    actions
  }