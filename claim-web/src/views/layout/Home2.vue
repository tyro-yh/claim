<template>
	<el-container>
		<el-header>
			<Header/>
		</el-header>
		<el-main>
			<router-view></router-view>
		</el-main>
	</el-container>
</template>

<script>
	import Header from '@/views/layout/Header2.vue'
	export default {
		components: { Header },
		data() {
			return {
				sysName:'非车理赔',
				sysUserName: '',
			}
		},
		methods: {
		},
		computed: {
		},
		mounted() {
			var user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.sysUserName = user.userName || '';
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
	
	.el-main {
		background-color: #E9EEF3;
		color: #333;
		text-align: center;
	}
</style>
