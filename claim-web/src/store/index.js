import Vue from 'vue'
import Vuex from 'vuex'

//挂载Vuex
Vue.use(Vuex)


//创建VueX对象
const store = new Vuex.Store({
    state: {
		tagList: [{
			path: "/main",
			name: "Main",
			meta: {
				closable: false,
				title: "主页"
			}
		}],
        collapse: false,
		asideWidth: '200px'
    },
	getters: {
	},
    mutations: {
        // 侧边栏折叠
        handleCollapse(state) {
            state.collapse = !state.collapse
			if(state.collapse) {
				state.asideWidth = '65px'
			} else {
				state.asideWidth = '200px'
			}
        },
		addTags(state,data) {
			// 判断是否已经存在
			const isExist = state.tagList.some((item) => {
				return item.path === data.path
			})
			// 如果不存在添加
			if (!isExist) {
				state.tagList.push(data)
			}
		},
		delTags(state, data) {
			state.tagList = state.tagList.filter(item => item.path !== data.path)
		},
		clossOther(state, data) {
			state.tagList = state.tagList.filter((item) => {
				return item.path == data.path || item.path == '/main'
			})
		},
		clossAll(state) {
			state.tagList = [{
				path: "/main",
				name: "Main",
				meta: {
					closable: false,
					title: "主页"
				}
			}]
		}
    },
    actions: {},
    modules: {}
})

export default store