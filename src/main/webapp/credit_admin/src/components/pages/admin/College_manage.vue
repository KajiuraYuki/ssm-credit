<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/admin/welcome' }" @click.native="saveNavState('/admin/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>综合管理</el-breadcrumb-item>
			  <el-breadcrumb-item>学院管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20">			
					<el-col :span="6">
						<el-input placeholder="请输入查找内容" clearable v-model="query"  @clear="getCollegeList()" >
							<el-button slot="append" icon="el-icon-search" @click="getCollegeList()" @clear="clearValues"></el-button>
						</el-input>					
					</el-col>										
					<!-- 添加学院数据按钮 -->
					<el-col :span="3">
					  <el-button type="primary" @click="addCollegeButton">添加学院</el-button>
					</el-col>
				</el-row>
				<!-- 用户信息列表区域 -->
				<el-table :data="CollegeList" ref="table" boder stripe :height="tableHeight">
					<!-- stripe  :  斑马条纹
						 border  :  边框
					-->
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="collegename" label="学院名称"></el-table-column>
					<el-table-column align="center" prop="ctime" label="添加时间"></el-table-column>
					<el-table-column align="center" label="操作" >
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.collegeid)">编辑</el-button>							
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="removeCollegeBycollegeid(scope.row.collegeid)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<!-- 信息条数提示 -->
				<el-pagination
				layout="total"
				:total="totle"></el-pagination>
			</el-card>
			<!-- 编辑学院信息对话框 -->
			<el-dialog center title="编辑学院信息" :visible.sync="editCollegeDialogVisible" width="40%" >
			  <el-form
			    :model="editCollegeForm"
			    ref="editCollegeForm"
			    :rules="editCollegeFormRules"
			    label-width="80px">
				<el-form-item disabled label="学院名称" prop="collegename">
				  <el-input v-model="editCollegeForm.collegename"></el-input>
				</el-form-item>			   
				<el-form-item label="创建时间" prop="ctime">
				  <el-input disabled v-model="editCollegeForm.ctime"></el-input>
				</el-form-item>	
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="editCollegeForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="editCollegeDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="editCollege()">确 定</el-button>
			  </span>
			</el-dialog>
			<!-- 添加学院信息对话框 -->
			<el-dialog center title="添加学院信息" :visible.sync="addCollegeDialogVisible" width="40%" >
			  <el-form
			    :model="addCollegeForm"
			    ref="addCollegeForm"
			    :rules="addCollegeFormRules"
			    label-width="80px">
				<el-form-item disabled label="学院名称" prop="collegename">
				  <el-input v-model="addCollegeForm.collegename"></el-input>
				</el-form-item>			   
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="addCollegeForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="addCollegeDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="addCollege()">确 定</el-button>
			  </span>
			</el-dialog>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'College_Manage',
		data() {
			return {
				tableHeight: 100,
				query: '',
				CollegeList: [],
				totle: 0,
				editCollegeDialogVisible: false,
				editCollegeForm: {},
				editCollegeFormRules: {
					collegename: [
						{ required: true, message: '学院名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '学院名称长度为 3 ~ 10个字!', trigger: 'change' }				
					]
				},
				addCollegeDialogVisible: false,
				addCollegeForm: {},
				addCollegeFormRules: {
					collegename: [
						{ required: true, message: '学院名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '学院名称长度为 3 ~ 10个字!', trigger: 'change' }				
					]
				}
				
			}
		},
		mounted: function() {
			this.$nextTick(function () {
				this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 95;
				let self = this;
				window.onresize = function() {
					self.tableHeight = window.innerHeight - self.$refs.table.$el.offsetTop - 95
				}
			})
		},created() {
			this.saveNavState('/admin/collegemanage')
			this.getCollegeList()			
		},
		methods: {
			// 获取学院信息列表
			getCollegeList() {
				this.$axios.post('admin/getallcollege',{
					query: this.query
				})
				.then(res => {
					if (res.status!== 200) {
						return this.$message.error("获取学院信息失败！")
					}
					this.CollegeList = res.data
					this.totle = res.data.length
				})
			},
			// 清空搜索数据
			clearValues() {
				this.query = ''				
			},
			// 添加学院 按钮点击事件
			addCollegeButton() {
				this.addCollegeDialogVisible = true				
			},
			// 修改学院信息
			editCollege() {
				this.$refs.editCollegeForm.validate(async valid => {
					if(valid) {
						this.$axios.put('admin/updatecollege/' + this.editCollegeForm.collegeid,this.editCollegeForm)
						.then(res => {
							if (res.status !== 200) {
								return this.$message.error("更新学院信息失败！")
							}
							this.editCollegeDialogVisible = false
							this.$message.success("更新学院信息成功！")
							this.getCollegeList()
						})
					}
				})
			},
			// 编辑按钮事件
			showEditDialog(collegeid) {
				this.$axios.post('admin/getcollegeinfo/' + collegeid)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取当前学院信息失败！")
					}
					this.editCollegeForm = res.data
					this.editCollegeDialogVisible = true
				})
			},
			// 添加学院信息
			addCollege() {
				this.$refs.addCollegeForm.validate(async valid => {
					if (valid) {
						this.$axios.post('admin/addcollege',this.addCollegeForm)
						.then(res => {
							//console.log(res)
							if (res.status !== 200) {
								return this.$message.error("添加学院信息失败！")
							}
							if (res.status === 200) {
								this.addCollegeDialogVisible = false
								this.$message.success("添加学院信息成功！")							
								this.getCollegeList()
							}									
						})
					}
				})	
			},
			// 根据学院id删除学院信息
			async removeCollegeBycollegeid(collegeid) {
				const confirmResult = await this.$confirm('此操作将永久删除该学院信息并删除与该学院相关的所有信息，是否继续？','提示',{
					confirmButtonText: '确定',
					cancelButtonText:  '取消',
					type: 'warning'
				}).catch(err => err)
				if(confirmResult !== 'confirm') {
					return this.$message.info('已取消删除！')
				}
				this.$axios.delete('admin/deletecollege/' + collegeid)
				.then(res => {
					if(res.status !== 200) {
						return this.$message.error('删除该学院信息失败！')
					}
					this.$message.success('删除该学院信息成功！')
					this.getCollegeList()
				})
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath',activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			}
		}
	}
</script>

<style scoped="scoped">
	#one {
		width: 100%;
		height: auto;		
	}
	#two {
		width: 100%;
		height: 100%;	
	}
	.el-card {
		height: 100%;
		width: 100%;
	}
	.el-pagination {
		text-align: left;
		padding-top: 20px;
	}
	#row1 {
		padding-top: 20px;
	}
</style>
