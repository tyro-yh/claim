<template>
	<section v-loading="loading">
		<el-row :gutter="20" style="line-height: 40px;">
			<el-col :offset="3" :span="3">业务动作</el-col>
			<el-col :span="4">
				<el-radio v-model="approveInfo.approveFlag" label="1" @change="handleChange" :disabled="approveStatus">同意</el-radio>
				<el-radio v-model="approveInfo.approveFlag" label="2" @change="handleChange" :disabled="approveStatus">退回</el-radio>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :offset="3" :span="3">审核类型</el-col>
			<el-col :span="4">
				<el-input v-model="approveInfo.approveTypeName" disabled></el-input>
			</el-col>
			<el-col :offset="3" :span="3">审核级别</el-col>
			<el-col :span="4">
				<el-input v-model="approveInfo.approveNode" disabled></el-input>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :offset="3" :span="3">审批意见</el-col>
			<el-col :span="14">
				<el-input
				  type="textarea"
				  placeholder="请输入内容"
				  v-model="approveInfo.content"
				  maxlength="500"
				  show-word-limit
				  :disabled="approveStatus"
				>
				</el-input>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :offset="3" :span="3">历史审批意见</el-col>
			<el-col :span="14">
				<el-input
				  type="textarea"
				  placeholder="请输入内容"
				  v-model="preContent"
				  disabled
				>
				</el-input>
			</el-col>
		</el-row>
		<el-row style="position: fixed;bottom: 10px;margin-left: 30%;">
			<el-button type="primary" @click="handleSave" :disabled="approveStatus">保存</el-button>
			<el-button type="primary" @click="handleSubmit" :disabled="approveStatus">保存并提交</el-button>
		</el-row>
	</section>
</template>

<script>
	import { initApproveInfo,saveApprove,submitApprove } from '@/api/api';
	export default {
		props: ['businessKey'],
		data() {
			return {
				approveInfo: {},
				loading: false,
				approveStatus: false,
				preContent: ''
			}
		},
		methods: {
			getApprove() {
				let params = {
					approveNo: this.businessKey
				};
				this.loading = true;
				initApproveInfo(params).then((res) => {
					this.approveInfo = res.data.data;
					if(this.approveInfo.approveDate) {
						this.approveStatus = true;
					}
					if(this.approveInfo.preContent.length > 0) {
						this.approveInfo.preContent.forEach((item) => {
							this.preContent += item+"\n";
						})
					}
					setTimeout(() => {
						this.loading = false;
					},500);
				});
			},
			handleChange() {
				if(this.approveInfo.approveFlag == '1') {
					this.approveInfo.content = '同意处理';
				} else {
					this.approveInfo.content = '退回修改';
				}
			},
			handleSave() {
				saveApprove(this.approveInfo).then((res) => {
					if(res) {
						this.$message.success("保存成功");
						this.getApprove();
					}
				});
			},
			handleSubmit() {
				submitApprove(this.approveInfo).then((res) => {
					if(res.data.data.status == '0') {
						this.$message.warning(res.data.data.msg);
					} else {
						this.$message.success("提交成功");
					}
					this.getApprove();
				});
			}
		},
		mounted() {
			this.getApprove();
		}
	}
</script>

<style>
</style>
