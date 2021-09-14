<template>
	<section>
		<el-row>
			<el-col :span="8">
				<el-card shadow="hover" v-loading="infoloading">
					<el-descriptions class="margin-top" title="基本信息" :column="2" border>
						<template slot="extra">
							<el-button-group>
							  <el-button type="primary" size="small" icon="el-icon-aim" @click.native="fitView">复位</el-button>
							  <el-button type="primary" size="small" icon="el-icon-download" @click.native="downloadGraph">导出</el-button>
							</el-button-group>
						</template>
					    <el-descriptions-item label="报案号">
					      {{reportNo}}
					    </el-descriptions-item>
					    <el-descriptions-item label="保单号">
					      {{reportMain.policyNo}}
					    </el-descriptions-item>
					    <el-descriptions-item label="险种">
					      <el-tag>{{reportMain.insuranceName}}</el-tag>
					    </el-descriptions-item>
					    <el-descriptions-item label="归属机构">
					      <el-tag type="success">{{reportMain.comName}}</el-tag>
					    </el-descriptions-item>
					</el-descriptions>
				</el-card>
			</el-col>
			<el-col :offset="1" :span="8">
				<el-card shadow="hover" style="height: 105px;display: flex;float: left;">
				<el-row :gutter="20">
				    <el-col :span="6">
						<span style="padding-left: 20px;height: 50px;">
							<div class="col-lump" style="background-color: #ff5500;"></div>
							<div style="padding-top: 3px;">待处理</div>
						</span>
					</el-col>
					<el-col :span="6">
						<span style="padding-left: 20px;height: 50px;">
							<div class="col-lump" style="background-color: #ffaa00;"></div>
							<div style="padding-top: 3px;">处理中</div>
						</span>
					</el-col>
					<el-col :span="6">
						<span style="padding-left: 20px;height: 50px;">
							<div class="col-lump" style="background-color: #2196f3;"></div>
							<div style="padding-top: 3px;">已处理</div>
						</span>
					</el-col>
					<el-col :span="6">
						<span style="padding-left: 20px;height: 50px;">
							<div class="col-lump" style="background-color: #666666;"></div>
							<div style="padding-top: 3px;">已退回</div>
						</span>
					</el-col>
				</el-row>
				</el-card>
			</el-col>
		</el-row>
		<el-card shadow="hover" style="margin-top: 20px;margin-bottom: 20px;" v-loading="graphloading">
			<div id="mountNode" :key="reportNo"></div>
		</el-card>
	</section>
</template>

<script>
	import { getWorkFlowList } from '@/api/api';
	import G6 from '@antv/g6';
	export default {
		name: "start",
		props: ['reportNo'],
		created() { //不能在created方法中初始化
		},
		mounted(){
			let user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.sysUserCode = user.userCode || '';
			}
			this.getWorkFlow();
		},
		computed:{
		},
		data() {
		    return {
				reportMain: {},
				graph1: '',
				sysUserCode: '',
				infoloading: false,
				graphloading: false
		    }
		},
		methods: {
			initG6(nodeItem){
				G6.registerNode("dom-node-1", {
					draw(cfg, group) {
						const width = 200;
						const height = 140;
						const keyShape = group.addShape('rect', {
							attrs: {
								x: 0,
								y: 0,
								width: 200,
								height: 140,
								radius: 5,
								stroke: cfg.color, // 颜色应用到描边上，如果应用到填充，则使用 fill: cfg.color
							},
							name: 'rect-parent',
							draggable: true,
						});
						group.addShape('rect', {
							attrs: {
								x: 0,
								y: 0,
								width: 200,
								height: 30,
								radius: [ 5, 5, 0, 0 ],
								fill: cfg.color,
							},
							name: 'rect-label',
							draggable: true,
						});
						group.addShape('text', {
							attrs: {
								x: 100, // 居中
								y: 15,
								textAlign: 'center',
								textBaseline: 'middle',
								text: cfg.label,
								fontSize: 18,
								fontWeight: 'bold',
								fill: '#fff',
							},
							name: 'text-label',
							draggable: true,
						});
						group.addShape('text', {
							attrs: {
								x: 100, // 居中
								y: 45,
								textAlign: 'center',
								textBaseline: 'middle',
								fontSize: 14,
								text: cfg.meta.link,
								fill: '#409EFF',
								cursor: 'pointer'
							},
							name: 'text-link',
							draggable: true,
						});
						group.addShape('text', {
							attrs: {
								x: 100, // 居中
								y: 75,
								textAlign: 'center',
								textBaseline: 'middle',
								fontSize: 12,
								text: cfg.meta.handlerLabel,
								fill: 'rgb(191, 203, 217)'
							},
							name: 'text-handler',
							draggable: true,
						});
						if(cfg.meta.startTime) {
							group.addShape('rect', {
								attrs: {
									x: 10,
									y: 90,
									width: 30,
									height: 15,
									radius: 3,
									stroke: '#2196f3', // 颜色应用到描边上，如果应用到填充，则使用 fill: cfg.color
								},
								name: 'rect-startTime',
								draggable: true,
							});
							group.addShape('text', {
								attrs: {
									x: 25, // 居中
									y: 97.5,
									textAlign: 'center',
									textBaseline: 'middle',
									fontSize: 12,
									text: '开始',
									fill: '#2196f3'
								},
								name: 'label-startTime',
								draggable: true,
							});
							group.addShape('text', {
								attrs: {
									x: 110, // 居中
									y: 97.5,
									textAlign: 'center',
									textBaseline: 'middle',
									fontSize: 12,
									text: cfg.meta.startTime,
									fill: '#2196f3'
								},
								name: 'text-startTime',
								draggable: true,
							});
						}
						if(cfg.meta.endTime) {
							group.addShape('rect', {
								attrs: {
									x: 10,
									y: 115,
									width: 30,
									height: 15,
									radius: 3,
									stroke: '#ff0000', // 颜色应用到描边上，如果应用到填充，则使用 fill: cfg.color
								},
								name: 'rect-endTime',
								draggable: true,
							});
							group.addShape('text', {
								attrs: {
									x: 25, // 居中
									y: 123.5,
									textAlign: 'center',
									textBaseline: 'middle',
									fontSize: 12,
									text: '结束',
									fill: '#ff0000'
								},
								name: 'label-endTime',
								draggable: true,
							});
							group.addShape('text', {
								attrs: {
									x: 110, // 居中
									y: 123.5,
									textAlign: 'center',
									textBaseline: 'middle',
									fontSize: 12,
									text: cfg.meta.endTime,
									fill: '#ff0000'
								},
								name: 'text-endTime',
								draggable: true,
							});
						}
						return keyShape;
					},
					getAnchorPoints() {
						return [
						  [0.5, 0],
						  [0.5, 1]
						];
					}
				},'single-node',);
				const graph = new G6.Graph({
					container: 'mountNode',
					width: 1400,
					height: 800,
					modes: {
					  default: ["zoom-canvas", "drag-canvas"]
					},
					defaultNode: {
						type: 'dom-node-1'
					},
					defaultEdge: {
						type: 'polyline',
						style: {
							endArrow: true,
							offset: 25,
							stroke: "#2196f3",
							lineWidth: 2
						},
					}
				});
				this.graph1 = graph;
				graph.data(nodeItem);
				graph.render();
				graph.fitCenter();
				graph.moveTo(10, 10, false);
				setTimeout(() => {
					this.graphloading = false;
				},500);
				
				graph.on('text-link:click', evt => {
					let model = evt.item._cfg.model;
					if(this.sysUserCode != model.meta.handler && model.meta.params.taskStatus == '1') {
						this.$message.warning("非本人任务无法进入");
						return false;
					}
					let routeUrl = this.$router.resolve({
						name: model.meta.taskType,
						params: {
							businessKey:model.meta.params.businessKey,
							reportNo:model.meta.params.reportNo
						}
					});
					window.open(routeUrl.href, '_blank');
				})
			},
			getWorkFlow() {
				let params = {
					reportNo: this.reportNo
				}
				this.infoloading = true;
				this.graphloading = true;
				getWorkFlowList(params).then((res) => {
					this.reportMain = res.data.data.reportMain;
					setTimeout(() => {
						this.infoloading = false;
					},500);
					let nodeItem = res.data.data.nodeItem;
					this.initG6(nodeItem);
				});
			},
			fitView() {
				this.graph1.fitCenter();
				this.graph1.moveTo(10, 10, true, {
				  duration: 100
				});
			},
			downloadGraph() {
				this.graph1.downloadFullImage('流程图-'+this.reportNo,'image/png',{
				  backgroundColor: '#fff'
				});
			}
		}
	}
</script>

<style>
	.col-lump {
		width: 60px;
		height: 25px;
	}
</style>
