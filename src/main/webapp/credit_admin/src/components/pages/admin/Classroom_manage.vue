<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/admin/welcome' }" @click.native="saveNavState('/admin/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>综合管理</el-breadcrumb-item>
			  <el-breadcrumb-item>教室信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20">
					<el-col :span="8">
						<el-input placeholder="请输入查找内容" clearable v-model="query"  @clear="getClassrooms()"  >
							<el-button slot="append" icon="el-icon-search" @click="getClassrooms()" @clear="clearValues"></el-button>
						</el-input>					
					</el-col>										
					<!-- 添加教室数据按钮 -->
					<el-col :span="3">
					  <el-button type="primary" @click="addClassRoomButton()">添加教室</el-button>
					</el-col>
				</el-row>
				<!-- 教室信息列表区域 -->
				<el-table :data="ClassroomList" ref="table" boder stripe :height="tableHeight">
					<!-- stripe  :  斑马条纹
						 border  :  边框
					-->
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="classroom" label="教室名称"></el-table-column>
					<el-table-column align="center" prop="content" label="描述"></el-table-column>
					<el-table-column align="center" prop="ctime" label="添加时间"></el-table-column>
					<el-table-column align="center" label="操作" >
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.classroom)">编辑</el-button>							
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="removeClassroomByname(scope.row.classroom)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<!-- 信息条数提示 -->
				<el-pagination
				layout="total"
				:total="totle"></el-pagination>
			</el-card>
			
			<!-- 添加教室信息对话框 -->
			<el-dialog center title="添加教室信息" :visible.sync="addClassroomDialogVisible" width="40%" @close="addDialogClosed">
			  <el-form
			    :model="addClassroomForm"
			    ref="addClassroomForm"
			    :rules="addClassroomFormRules"
			    label-width="80px">
				<el-form-item disabled label="教室名称" prop="classroom">
				  <el-input type="text" v-model="addClassroomForm.classroom"></el-input>
				</el-form-item>			   
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="addClassroomForm.content" placeholder="暂无描述"/>
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="addClassroomDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="addClassroom()">确 定</el-button>
			  </span>
			</el-dialog>
			
			
			<!-- 编辑教室信息对话框 -->
			<el-dialog center title="编辑教室信息" :visible.sync="editClassroomDialogVisible" width="40%" @close="editDialogClosed">
			  <el-form
			    :model="editClassroomForm"
			    ref="editClassroomForm"
			    label-width="80px">
				<el-form-item disabled label="教室名称" prop="classroom">
				  <el-input disabled type="text" v-model="editClassroomForm.classroom"></el-input>
				</el-form-item>			   
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="editClassroomForm.content"/>
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="editClassroomDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="editClassroom()">确 定</el-button>
			  </span>
			</el-dialog>
			
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'Classroom_manage',
		data() {
			// 教室名称验证规则
			var checkClassroom = (rule, value, callback) => {
			  const regClassroom = /^(?!0)\d{1,3}-\d{3,}$/
			  if (regClassroom.test(value)) {
			    // 合法教室名称
			    return callback()
			  }
			  callback(new Error('请输入合法教室名称!如: 15-205'))
			}
			
			
			return {
				tableHeight: 100,
				query: '',
				ClassroomList: [],
				totle: 0,
				addClassroomDialogVisible: false,
				editClassroomDialogVisible: false,
				addClassroomForm: {},
				editClassroomForm: {},
				addClassroomFormRules: {
					classroom: [
						{ required: true, message: '教室名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '教室名称长度为 3 ~ 10个字!', trigger: 'change' },
						{ validator: checkClassroom }
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
		},
		created() {
			this.saveNavState('/admin/crmanage')
			this.getClassrooms()
		},
		methods: {
			
			addClassroom() {
				// 对当前即将添加的教室进行查重，看数据库中是否已经有了该数据
				this.$axios.post('admin/classroomcheck2/' + this.addClassroomForm.classroom)
				.then(res => {
					console.log(res)
					if (res.data.code === 0) {
						// 执行添加过程
						this.$refs.addClassroomForm.validate(async valid => {
							if (valid) {
								this.$axios.post('admin/addclassroom',this.addClassroomForm)
								.then(res => {
									if (res.status !== 200) {
										return this.$message.error("添加教室信息失败！")
									}
									if (res.status === 200) {
										this.addClassroomDialogVisible = false
										this.$message.success("添加教室信息成功！")							
										this.getClassrooms()
									}									
								})
							}
						})	
					} else {
						this.$message.error("已经存在该数据，请勿重复添加！")
					}
				})
				
				
			},
			// 添加教室 按钮点击事件
			addClassRoomButton() {
				this.addClassroomDialogVisible = true				
			},
			// 清空搜索数据
			clearValues() {
				this.query = ''				
			},
			getClassrooms() {
				this.$axios.post('admin/getallcr',{
					query: this.query
				}).then(res => {
					if (res.status!== 200) {
						return this.$message.error("获取教室信息失败！")
					}
					this.ClassroomList = res.data
					this.totle = res.data.length
				})
			},			
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath',activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			},
			// 监听 添加教室信息 对话框关闭事件
			addDialogClosed() {
				//取消表验证
				this.$refs['addClassroomForm'].resetFields();				
			},
			// 监听 编辑教室信息 对话框关闭事件
			editDialogClosed() {
				//取消表验证
				this.$refs['editClassroomForm'].resetFields();
			},
			
			// 删除当起行数据
			async removeClassroomByname(classroomname) {
			   const confirmResult = await this.$confirm('此操作将永久删除该数据，是否继续？',
				'提示',
				{
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).catch(err => err)
				//console.log(confirmResult)
				if (confirmResult !== 'confirm'){
					return this.$message.info("已取消删除")
				}
				this.$axios.delete("admin/deleteclassroom/" + classroomname)
				.then(res => {
					if (res.status !== 200) {
						this.$message.error("删除数据失败")
					}
				this.$message.success("删除数据成功！")
				this.getClassrooms()
				})
			},
			// 点击编辑按钮 打开编辑对话框，显示当前行的数据
			showEditDialog(classroomname) {
				this.$axios.post('admin/classroomcheck/' + classroomname)
				.then(res => {
				  console.log("res===?" + res)
					if (res.status === 200) {
						this.editClassroomForm = res.data.data[0]
						this.editClassroomDialogVisible = true
					} else {
						this.$message.error("获取教室信息失败！")
					}
				})
			},
			// 点击编辑按钮的确定按钮
			editClassroom() {
				this.$refs.editClassroomForm.validate(async valid => {
					if (valid) {
						this.$axios.put("admin/updateclassroom" , this.editClassroomForm)
						.then(res => {
							if (res.status !== 200) {
								return this.$message.error("更新教室信息失败！")
							}
							this.editClassroomDialogVisible = false
							this.$message.success("更新教室信息成功！")
							this.getClassrooms()
						})
					}
				})	
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
