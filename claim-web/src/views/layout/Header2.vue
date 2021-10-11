<template>
	<el-row>
		<el-col :span="1">
		</el-col>
		<el-col :span="3" style="font-size: 22px;text-align: left">
			{{$route.meta.title}}
		</el-col>
		<el-col :offset="15" :span="5">
			<div class="userinfo">
				<el-dropdown trigger="hover">
					<span class="el-dropdown-link userinfo-inner"><img src="@/assets/img/img.jpg" />
						{{user.userName}}<i class="el-icon-caret-bottom"></i>
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item>我的消息</el-dropdown-item>
						<el-dropdown-item>设置</el-dropdown-item>
						<el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</div>
		</el-col>
	</el-row>
</template>

<script>
	import { removeRedis } from '@/api/api';
	export default {
		data() {
			return {
				user: {}
			}
		},
		computed: {
		},
		methods: {
			logout() {
				let params = {
					key: this.user.token
				}
				removeRedis(params).then((res) => {
					this.$router.push("/login");
				});
			}
		},
		mounted() {
			let user = localStorage.getItem('user');
			if(user) {
				this.user = JSON.parse(user);
			}
		}
	}
</script>

<style>
	.userinfo {
		text-align: right;
		padding-right: 20px;
		float: right;
	}
	
	.userinfo .userinfo-inner {
		cursor: pointer;
	}
	
	.userinfo .userinfo-inner img {
		width: 40px;
		height: 40px;
		border-radius: 20px;
		margin: 10px 0px 10px 10px;
		float: right;
	}
</style>
