<template>
	<el-container>
		<!-- 布局侧边栏 -->
		<el-aside :width="asideWidth">
			<Sidebar/>
		</el-aside>
		<el-container>
			<!-- 布局顶栏 -->
			<el-header>
				<Header/>
			</el-header>
			<div class="tags">
				<Tags/>
			</div>
			<!-- 布局主要区域 -->
			<el-main>
				<router-view v-slot="{ Component }">
				    <transition name="move" mode="out-in">
				        <keep-alive :include="tagList">
				            <component :is="Component" />
				        </keep-alive>
				    </transition>
				</router-view>
			</el-main>
		</el-container>
	</el-container>
</template>

<script>
	import Header from '@/views/layout/Header.vue'
	import Sidebar from '@/views/layout/Sidebar.vue'
	import Tags from '@/views/layout/Tags.vue'
	export default {
		components: { Header,Sidebar,Tags },
		data() {
			return {
				sysName:'非车理赔',
				sysUserName: '',
			}
		},
		methods: {
		},
		computed: {
			collapsed() {
				return this.$store.state.collapse
			},
			asideWidth() {
				return this.$store.state.asideWidth
			},
			tagList() {
				return this.$store.state.tagList
			}
		},
		beforeRouteUpdate(to, from, next) {
			this.$store.commit("addTags",to);
			next();
		},
		mounted() {
			var user = localStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.sysUserName = user.userName || '';
			}
			if(this.$route.name != 'Main') {
				this.$store.commit("addTags",this.$route);
			}
		}
	}
</script>

<style>
	.el-container {
		height: 100%;
		overflow: hidden;
	}
	
	.el-header {
		height: 60px;
		line-height: 60px;
		border-bottom: 1px solid;
		text-align: right;
		overflow: hidden;
	}
	  
	.el-aside {
		background: #324157;
		color: #fff;
		text-align: left;
		overflow: hidden;
		height: 100%;
	}
	
	.el-main {
		background-color: #E9EEF3;
		color: #333;
		text-align: center;
	}
	  
	body > .el-container {
		margin-bottom: 40px;
	}

	.tags {
		height: 40px;
		line-height: 40px;
		border-bottom: 1px solid;
	}
</style>
