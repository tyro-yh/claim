<template>
	<div class="sidebar">
		<!--导航菜单-->
		<el-menu :default-active="$route.path" class="el-menu-vertical-demo"
			 unique-opened router background-color="#324157"
			text-color="#bfcbd9" active-text-color="#20a0ff" :collapse="collapsed">
			<template v-for="(item,index) in menuList" v-if="!item.hidden">
				<el-submenu :index="index+''" v-if="item.children && item.children.length > 0">
					<template slot="title">
						<i :class="item.iconCls"></i>
						<span slot="title">{{item.meta.title}}</span>
					</template>
					<el-menu-item v-for="child in item.children" :index="child.path" :key="child.title" v-if="!child.hidden">
						<span slot="title">{{child.meta.title}}</span>
					</el-menu-item>
				</el-submenu>
				<el-menu-item :index="item.path" :key="item.meta.title" v-if="!item.children || item.children.length == 0">
					<i :class="item.iconCls"></i>
					<span slot="title">{{item.meta.title}}</span>
				</el-menu-item>
			</template>
		</el-menu>
	</div>
</template>

<script>
  import { getMenuList } from '@/api/api';
	export default {
		data() {
			return {
        menuList: []
				/* menuList: [
					{
						path: "/main",
						name: "Main",
						meta: {
							closable: false,
							title: "主页"
						},
						iconCls: 'el-icon-s-home',
					},
					{
						path: "/",
						name: "Dict",
						meta: {
							closable: false,
							title: "数据字典"
						},
						iconCls: 'el-icon-s-order',
						children: [
							{
								path: "/user",
								name: "User",
								meta: {
									closable: true,
									title: "员工中心"
								}
							},
							{
								path: "/company",
								name: "Company",
								meta: {
									closable: true,
									title: "组织架构"
								}
							},
							{
								path: "/common",
								name: "Common",
								meta: {
									closable: true,
									title: "其他配置"
								}
							}
						]
					},
					{
						path: "/",
						name: "Claim",
						meta: {
							closable: false,
							title: "案件处理"
						},
						iconCls: 'el-icon-s-claim',
						children: [
							{
								path: "/reportEdit",
								name: "ReportEdit",
								meta: {
									closable: true,
									title: "报案"
								}
							},
							{
								path: "/policySearch",
								name: "PolicySearch",
								meta: {
									closable: true,
									title: "保单查询"
								}
							}
						]
					},
					{
						path: "/processManage",
						name: "ProcessManage",
						meta: {
							closable: false,
							title: "流程管理"
						},
						iconCls: 'el-icon-share'
					},
          {
          	path: "/gradeManage",
          	name: "GradeManage",
          	meta: {
          		closable: false,
          		title: "岗位管理"
          	},
          	iconCls: 'el-icon-share'
          },
					{
						path: "/analysis",
						name: "Analysis",
						meta: {
							closable: false,
							title: "数据分析"
						},
						iconCls: 'el-icon-s-data'
					}
				] */
			}
		},
		computed: {
			collapsed() {
				return this.$store.state.collapse
			}
		},
		methods: {
      getMenu() {
        getMenuList().then(res => {
          this.menuList = res.data.data;
        })
      }
		},
		mounted() {
      this.getMenu();
		}
	}
</script>

<style>
	.sidebar {
	    display: block;
		padding: 0;
	    left: 0;
		height: 100%;
	}

	.el-menu-vertical-demo:not(.el-menu--collapse) {
	    width: 200px;
	}

	.el-menu--collapse {
	    width: 65px;
	}
</style>
