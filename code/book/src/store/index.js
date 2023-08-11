import Vue from 'vue' //引入vue
import Vuex from 'vuex' //引入vuex(后面的vuex必须小写)
import user from './user'

Vue.use(Vuex); //使用vuex插件
//用于存储数据
const state={

}
//用于响应动作
const actions={

}
//用于操作数据
const mutations={

}

//创建store、暴露store，传入一个对象
const store = new Vuex.Store({
    actions,
    mutations,
    state,
    user,
})
export default store