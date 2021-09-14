<template>
	<section>
		<el-card shadow="hover" style="line-height: 40px;">
			<el-row>
				<el-col :span="1">报案号</el-col>
				<el-col :span="3">
					<el-input size="medium" v-model="reportNo" disabled></el-input>
				</el-col>
			</el-row>
		</el-card>
		<div style="margin-top: 20px;">
			<el-menu default-active="Base" class="el-menu-demo" mode="horizontal" @select="handleSelect">
				<el-menu-item index="Base">案件基本信息</el-menu-item>
				<el-menu-item index="Claim">立案处理</el-menu-item>
				<el-menu-item index="Settlement">理算处理</el-menu-item>
				<el-menu-item index="4">资料收集</el-menu-item>
				<el-submenu index="5">
					<template slot="title">更多</template>
					<el-menu-item index="5-1">人伤处理</el-menu-item>
					<el-menu-item index="5-2">查勘处理</el-menu-item>
					<el-menu-item index="5-3">注销与拒赔处理</el-menu-item>
					<el-menu-item index="5-4">案件重开</el-menu-item>
				</el-submenu>
			</el-menu>
			<div style="margin-top: 10px;">
				<transition name="fade" mode="out-in" appear>
					<keep-alive>
					    <component :is="activeComponent" :reportNo="reportNo" :caseFlag="caseFlag"/>
					</keep-alive>
				</transition>
			</div>
		</div>
		<div class="navigation">
			<div class="bar">
				<div class="bar-icon">
					<i class="el-icon-s-tools"></i>
				</div>
				<div class="bar-text" align="left">
					<span @click="endCase()">申请结案</span>
				</div>
			</div>
		</div>
	</section>
</template>

<script>
	import Base from '@/views/claim/Base.vue'
	import Claim from '@/views/claim/Claim.vue'
	import Settlement from '@/views/claim/Settlement.vue'
	import { getWorkFlowList,applyEndCase,initCaseFlag } from '@/api/api';
	export default {
		props: ['reportNo'],
		mounted(){
			this.getCaseFlag();
		},
		components: {
			Base,
			Claim,
			Settlement
		},
		data() {
		    return {
				activeComponent: 'Base',
				collapse: false,
				caseFlag: ''
		    }
		},
		methods: {
			handleSelect(key, keyPath) {
				this.activeComponent = key;
			},
			endCase() {
				if(this.caseFlag != '04') {
					this.$confirm('是否申请结案?', '申请结案', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						let params = {
							reportNo: this.reportNo
						}
						applyEndCase(params).then((res) => {
							if(res.data.data.status == '0') {
								this.$message.warning(res.data.data.msg);
							} else {
								this.$message.success('提交成功');
							}
						})
					}).catch(() => {
						this.$message.info('已取消');
					});
				} else {
					this.$message.warning("该案件已结案");
				}
			},
			getCaseFlag() {
				let params = {
					reportNo: this.reportNo
				}
				initCaseFlag(params).then((res) => {
					this.caseFlag = res.data.data;
				})
			}
		}
	}
</script>

<style>
	@import url("../../assets/css/navigation.css");
</style>
