<template>
	<section>
		<el-card>
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="流程类型">
						<el-select v-model="filters.dataType">
							<el-option label="立案审核" value="ClaimApprove"></el-option>
							<el-option label="理算审核" value="Undwrt"></el-option>
							<el-option label="结案审核" value="EndCase"></el-option>
							<el-option label="人伤跟踪审核" value="PersonApprove"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="getProcess">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="dialogVisible = true">新增</el-button>
					</el-form-item>
				</el-form>
			</el-col>
			
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="loading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="process.processType" label="流程类型" width="120" :formatter="formatType">
				</el-table-column>
				<el-table-column prop="process.title" label="流程标题" min-width="120">
				</el-table-column>
				<el-table-column prop="process.createTime" label="创建时间" width="100" :formatter="formatCreateTime">
				</el-table-column>
				<el-table-column prop="process.updateTime" label="更新时间" width="100" :formatter="formatUpdateTime">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-card>
		
		<el-card shadow="hover" style="margin-top: 20px;text-align: left;">
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="process" class="demo-form-inline">
					<el-form-item label="流程标类型">
						<el-select v-model="process.processType" disabled>
							<el-option label="立案审核" value="ClaimApprove"></el-option>
							<el-option label="理算审核" value="Undwrt"></el-option>
							<el-option label="结案审核" value="EndCase"></el-option>
							<el-option label="人伤跟踪审核" value="PersonApprove"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="流程标题">
						<el-input v-model="process.title" placeholder="请输入流程标题"></el-input>
					</el-form-item>
					<el-form-item label="险种">
						<el-select v-model="process.insurance" multiple collapse-tags placeholder="请选择">
							<el-option value="01" label="家庭财产保险"></el-option>
							<el-option value="02" label="意外保险"></el-option>
							<el-option value="03" label="短期健康保险"></el-option>
						</el-select>
					</el-form-item>
					<el-button type="primary" @click="addNode">添加审核</el-button>
					<el-button type="primary" @click="delNode">删除审核</el-button>
					<el-button type="primary" @click="handleSave">保存流程</el-button>
				</el-form>
			</el-col>
		</el-card>
		
		<el-row :gutter="24">
			<el-col :span="18">
				<el-card shadow="hover" style="margin-top: 20px;">
					<div style="width: 1000px;" id="mountNode" :key="mountId"></div>
				</el-card>
			</el-col>
			<el-col :span="6">
				<el-card shadow="hover" style="margin-top: 20px;" v-if="model.id">
					<el-form :model="model" size="small">
						<el-form-item label="处理人">
							<el-select v-model="model.handler" placeholder="请选择">
								<el-option value="HO0000" label="管理员"></el-option>
								<el-option value="SH0001" label="杨皓"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="权限金额（含）">
							<el-input-number v-model="model.amount" :min="0" :controls="false"></el-input-number>
						</el-form-item>
						<!-- <el-button size="small" @click="saveHandler">保存</el-button> -->
					</el-form>
				</el-card>
			</el-col>
		</el-row>
		
		<el-dialog
		  title="提示"
		  :visible.sync="dialogVisible"
		  width="30%">
		  请选择审核类型
		  <el-select v-model="filters.dataType">
		  	<el-option label="立案审核" value="ClaimApprove"></el-option>
		  	<el-option label="理算审核" value="Undwrt"></el-option>
        <el-option label="结案审核" value="EndCase"></el-option>
        <el-option label="人伤跟踪审核" value="PersonApprove"></el-option>
		  </el-select>
		  <span slot="footer" class="dialog-footer">
		    <el-button @click="dialogVisible = false">取 消</el-button>
		    <el-button type="primary" @click="handleAdd">确 定</el-button>
		  </span>
		</el-dialog>
	</section>
</template>

<script>
	import { saveProcess,getProcessList } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	import G6 from '@antv/g6';
	export default {
		data() {
			return {
				loading: false,
				dialogVisible: false,
				filters: {
					dataType: ''
				},
				mountId: '0',
				process: {
					id: 0
				},
				nodeItem: {
					nodes: [],
					edges: []
				},
				model: {
					
				},
				tableData: [],
				graphTool: null,
				nx: 0,
				ny: 0,
				nid: '0'
			}
		},
		methods: {
			formatType(row, column) {
				let typeName;
				switch(row.process.processType) {
					case 'ClaimApprove': typeName = '立案审核';break;
					case 'Undwrt': typeName = '理算审核';break;
					case 'EndCase': typeName = '结案审核';break;
					case 'PersonApprove': typeName = '人伤跟踪审核';break;
				};
				return typeName;
			},
			formatCreateTime(row, column) {
				return formatTimeToStr(row.process.createTime, 'yyyy-MM-dd');
			},
			formatUpdateTime(row, column) {
				return formatTimeToStr(row.process.updateTime, 'yyyy-MM-dd');
			},
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			getProcess() {
				let params = {
					processType: this.filters.dataType
				};
				this.loading = true;
				getProcessList(params).then((res) => {
					this.tableData = res.data.data;
					setTimeout(() => {
						this.loading = false;
					},500);
				});
			},
			handleEdit(index,row) {
				this.process = {
					id: row.process.id,
					processType: row.process.processType,
					title: row.process.title,
					insurance: row.process.insurance
				};
				
				// this.mountId = this.process.id;
				let x = 0;
				let y = 0;
				row.nodes.forEach((item) => {
					if(parseInt(item.id) > 0 && parseInt(item.id) % 4 == 0) {
						y = y + 150;
						x = 0;
					}
					item.x = x;
					item.y = y;
					
					this.nx = x;
					this.ny = y;
					this.nid = item.id;
					
					x = x + 250;
				});
				this.nodeItem.nodes = row.nodes;
				
				let edges = [];
				for(var i = 1;i<row.nodes.length;i++) {
					let edge = {
						source: i-1+"",
						target: i+"",
						sourceAnchor: 1,
						targetAnchor: 0
					}
					
					if(i % 4 == 0) {
						edge = {
							source: i-1+"",
							target: i+"",
							sourceAnchor: 3,
							targetAnchor: 2
						}
					}
					edges.push(edge);
				}
				this.nodeItem.edges = edges;
				
				if(this.graphTool) {
					this.graphTool.changeData(this.nodeItem);
				} else {
					this.initGraph();
				}
			},
			handleAdd() {
				this.dialogVisible = false;
				this.process = {
					processType: this.filters.dataType
				};
				this.nx = 0;
				this.ny = 0;
				this.nid = '0';
				this.nodeItem.nodes = [{
					id: '0',
					text: '根节点',
					x: 0,
					y: 0,
				}];
				this.nodeItem.edges = [];
				
				if(this.graphTool) {
					this.graphTool.changeData(this.nodeItem);
				} else {
					this.initGraph();
				}
			},
			initGraph() {
				G6.registerNode("dom-node-1", {
					draw(cfg, group) {
						const width = 150;
						const height = 80;
						const keyShape = group.addShape('rect', {
							attrs: {
								x: 0,
								y: 0,
								width: 150,
								height: 80,
								radius: 5,
								stroke: '#2196f3', // 颜色应用到描边上，如果应用到填充，则使用 fill: cfg.color
							},
							name: 'rect-parent',
							draggable: true,
						});
						group.addShape('rect', {
							attrs: {
								x: 0,
								y: 0,
								width: 20,
								height: 80,
								radius: [ 5, 0, 0, 5 ],
								fill: '#2196f3',
							},
							name: 'rect-label',
							draggable: true,
						});
						group.addShape('text', {
							attrs: {
								x: 75, // 居中
								y: 40,
								textAlign: 'center',
								textBaseline: 'middle',
								text: cfg.text,
								fontSize: 18,
								fontWeight: 'bold',
								fill: '#409EFF',
								cursor: 'pointer'
							},
							name: 'text-label',
							draggable: true,
						});
						return keyShape;
					},
					getAnchorPoints() {
						return [
						  [0, 0.5],
						  [1, 0.5],
						  [0.5,0],
						  [0.5,1]
						];
					}
				},'single-node',);
				const graph = new G6.Graph({
					container: 'mountNode',
					width: 1000,
					height: 500,
					modes: {
					  // default: ["zoom-canvas", "drag-canvas"]
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
					},
				});
				
				graph.data(this.nodeItem);
				graph.render();
				graph.on('node:click', evt => {
					let model = evt.item._cfg.model;
					this.nodeItem.nodes.forEach((item) => {
						if(item.id == model.id) {
							this.model = item;
						}
					})
				})
				
				this.graphTool = graph;
			},
			addNode() {
				this.nx = this.nx + 250;
				let source = this.nid;
				this.nid = (parseInt(this.nid) + 1)+'';
				let target = this.nid;
				if(parseInt(this.nid) % 4 == 0) {
					this.ny = this.ny + 150;
					this.nx = 0;
				}
				const model = {
					id: this.nid,
					text: '审核'+this.nid+'级',
					x: this.nx,
					y: this.ny
				};
				
				this.nodeItem.nodes.push(model);
				
				this.graphTool.addItem('node', model);
				
				let edge = {
					source: source,
					target: target,
					sourceAnchor: 1,
					targetAnchor: 0
				};
				
				if(parseInt(this.nid) % 4 == 0) {
					edge = {
						source: source,
						target: target,
						sourceAnchor: 3,
						targetAnchor: 2
					}
				}
				this.graphTool.addItem('edge', edge);
			},
			delNode() {
				if(this.nid == '0') {
					return false;
				}
				const item = this.graphTool.findById(this.nid);
				this.graphTool.removeItem(item);
				
				this.nodeItem.nodes.pop();
				
				this.nid = (parseInt(this.nid) - 1)+'';
				const item2 = this.graphTool.findById(this.nid);
				this.nx = item2.get('model').x;
				this.ny = item2.get('model').y;
			},
			saveHandler() {
				this.nodeItem.nodes.forEach((item) => {
					if(item.id == this.model.id) {
						item.handler = this.model.handler;
						item.amount = this.model.amount;
					}
				})
			},
			handleSave() {
				let params = {
					process: this.process,
					nodes: this.nodeItem.nodes
				}
				saveProcess(params).then((res) => {
					this.$message.success("保存成功");
					this.getProcess();
				})
			}
		},
		mounted() {
		}
	}
</script>

<style>
</style>