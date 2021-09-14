<template>
	<el-row>
		<el-col :span="1">
			<div class="collapseBtn" @click.prevent="handleCollapse">
				<i v-if="collapsed" class="el-icon-s-unfold"></i>
				<i v-if="!collapsed" class="el-icon-s-fold"></i>
			</div>
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
						<el-dropdown-item @click="drawer = true">
							<el-badge :value="1" :max="99" class="item">
							我的消息<i class="el-icon-message-solid"></i>
							</el-badge>
						</el-dropdown-item>
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
				drawer: false,
				user: {}
			}
		},
		computed: {
			collapsed() {
				return this.$store.state.collapse
			}
		},
		methods: {
			handleCollapse() {
				this.$store.commit("handleCollapse");
			},
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
			let user = sessionStorage.getItem('user');
			if(user) {
				this.user = JSON.parse(user);
			}
		}
	}
</script>

<style>
	.collapseBtn {
		width: 14px;
		height: 60px;
		line-height: 60px;
		cursor: pointer;
		font-size: 22px;
	}

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
