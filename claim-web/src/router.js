import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/common/Login.vue'
import Home from '@/views/layout/Home.vue'
import Home2 from '@/views/layout/Home2.vue'
Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function (location) {
  return originalPush.call(this, location).catch(err => err);
};

const routes = [
	{
		path: '/login',
		name: 'Login',
		meta: {
			title: "登录"
		},
		props: true,
		component: Login
	},
	{
		path: '/',
		name: 'Home',
		meta: {
			title: "我的"
		},
		iconCls: 'el-icon-s-home',//图标样式class
		component: Home,
		children: [
			{
				path: "/main",
				name: "Main",
				meta: {
					closable: false,
					title: "主页"
				},
				iconCls: 'el-icon-s-home',
				component: () => import(/* webpackChunkName: "about" */ '@/views/common/Main.vue')
			},
			{
				path: "/user",
				name: "User",
				meta: {
					closable: true,
					title: "员工中心"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/dictionary/User.vue')
			},
			{
				path: "/company",
				name: "Company",
				meta: {
					closable: true,
					title: "组织架构"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/dictionary/Company.vue')
			},
			{
				path: "/common",
				name: "Common",
				meta: {
					closable: true,
					title: "其他配置"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/dictionary/Common.vue')
			},
			{
				path: "/reportEdit",
				name: "ReportEdit",
				meta: {
					closable: true,
					title: "报案"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/ReportEdit.vue')
			},
			{
				path: "/policySearch",
				name: "PolicySearch",
				meta: {
					closable: true,
					title: "保单查询"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/PolicySearch.vue')
			},
			{
				path: "/processManage",
				name: "ProcessManage",
				meta: {
					closable: true,
					title: "流程管理"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/common/ProcessManage.vue')
			},
      {
      	path: "/gradeManage",
      	name: "GradeManage",
      	meta: {
      		closable: true,
      		title: "岗位管理"
      	},
      	component: () => import(/* webpackChunkName: "about" */ '@/views/common/GradeManage.vue')
      },
			{
				path: "/analysis",
				name: "Analysis",
				meta: {
					closable: true,
					title: "数据分析"
				},
				component: () => import(/* webpackChunkName: "about" */ '@/views/common/Analysis.vue')
			}
		]
	},
	{
		path: '/',
		name: 'Home2',
		meta: {
			title: "我的"
		},
		iconCls: 'el-icon-s-home',//图标样式class
		component: Home2,
		children: [
			{
				path: "/workflow/:reportNo",
				name: "Workflow",
				meta: {
					title: "流程图"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/common/Workflow.vue'),
			},
			{
				path: "/report/:reportNo",
				name: "Report",
				meta: {
					title: "报案"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/ReportShow.vue')
			},
			{
				path: "/caseMain/:reportNo",
				name: "CaseMain",
				meta: {
					title: "案件受理"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/CaseMain.vue')
			},
			{
				path: "/cApprove/:businessKey/:reportNo/:handler",
				name: "ClaimApprove",
				meta: {
					title: "立案审核"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/ClaimApprove.vue')
			},
			{
				path: "/undwrt/:businessKey/:reportNo/:handler",
				name: "Undwrt",
				meta: {
					title: "核赔"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/undwrt/Undwrt.vue')
			},
			{
				path: "/settlementDetail",
				name: "SettlementDetail",
				meta: {
					title: "理算"
				},
				props: route => ({
						reportNo: route.query.reportNo,
						settlementType: route.query.settlementType,
						settlementNo: route.query.settlementNo
					}),
				component: () => import(/* webpackChunkName: "about" */ '@/views/claim/SettlementDetail.vue')
			},
			{
				path: "/endCase/:businessKey/:reportNo/:handler",
				name: "EndCase",
				meta: {
					title: "结案审核"
				},
				props: true,
				component: () => import(/* webpackChunkName: "about" */ '@/views/endCase/EndCase.vue')
			},
      {
      	path: "/personDetail",
      	name: "PersonDetail",
      	meta: {
      		title: "人伤跟踪"
      	},
      	props: route => ({
      			reportNo: route.query.reportNo,
      			personId: route.query.personId
      		}),
      	component: () => import(/* webpackChunkName: "about" */ '@/views/person/PersonDetail.vue')
      },
      {
      	path: "/personApprove/:businessKey/:reportNo/:handler",
      	name: "PersonApprove",
      	meta: {
      		title: "人伤跟踪审核"
      	},
      	props: true,
      	component: () => import(/* webpackChunkName: "about" */ '@/views/person/PersonApprove.vue')
      },
		]
	}
]

const router = new VueRouter({
	mode: 'history',
	routes: routes
})

router.beforeEach((to, from, next) => {
  //NProgress.start();
  if (to.path == '/login') {
    localStorage.removeItem('user');
  }
  let user = JSON.parse(localStorage.getItem('user'));
  if (!user && to.path != '/login') {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router
