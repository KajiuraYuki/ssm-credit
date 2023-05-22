<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/admin/welcome' }" @click.native="saveNavState('/admin/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>综合管理</el-breadcrumb-item>
			  <el-breadcrumb-item>专业管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20">			
					<el-col :span="6">
						<el-input placeholder="请输入查找内容" clearable v-model="query" clearable @clear="getMajorList()" >
							<el-button slot="append" icon="el-icon-search" @click="getMajorList()" @clear="clearValues"></el-button>
						</el-input>					
					</el-col>
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="select_college" placeholder="请选择学院" >
					    <el-option
					      v-for="item1 in college"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<!-- 搜索按钮 -->
					<el-col :span="2">
					  <el-button type="primary" @click="getMajorList()">搜索</el-button>
					</el-col>
					<!-- 清空搜索按钮 -->
					<el-col :span="3">
					  <el-button type="info" @click="clearValues">清除搜索</el-button>
					</el-col>
					<!-- 添加专业数据按钮 -->
					<el-col :span="3">
					  <el-button type="primary" @click="addMajorButton">添加专业</el-button>
					</el-col>
				</el-row>
				<!-- 专业信息列表区域 -->
				<el-table :data="MajorList" ref="table" boder stripe :height="tableHeight">
					<!-- stripe  :  斑马条纹
						 border  :  边框
					-->
					<el-table-column align="center" type="index" label="#"></el-table-column>					
					<el-table-column align="center" prop="collegename" label="所属专业"></el-table-column>
					<el-table-column align="center" prop="majorname" label="专业名称"></el-table-column>
					<el-table-column align="center" prop="ctime" label="添加时间"></el-table-column>
					<el-table-column align="center" label="操作" >
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.majorid)">编辑</el-button>							
							<el-button type="danger" icon="el-icon-delete" size="mini" @click="removeMajorBymajorid(scope.row.majorid)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<!-- 信息条数提示 -->
				<el-pagination
				layout="total"
				:total="totle"></el-pagination>
			</el-card>
			<!-- 编辑专业信息对话框 -->
			<el-dialog center title="编辑专业信息" :visible.sync="editMajorDialogVisible" width="40%" >
			  <el-form
			    :model="editMajorForm"
			    ref="editMajorForm"
			    :rules="editMajorFormRules"
			    label-width="80px">
				<el-form-item disabled label="专业名称" prop="majorname">
				  <el-input v-model="editMajorForm.majorname"></el-input>
				</el-form-item>
			    <el-form-item disabled label="所属学院" prop="collegename">
			      <el-select v-model="editMajorForm.collegename" placeholder="请选择所属学院">
			      		<el-option
							v-for="item in CollegeData"
			      			:key="item.collegeid"
			      			:label="item.collegename"
			      			:value="item.collegeid">
						</el-option>
			      </el-select>
			    </el-form-item>
				<el-form-item label="创建时间" prop="ctime">
				  <el-input disabled v-model="editMajorForm.ctime"></el-input>
				</el-form-item>	
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="editMajorForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="editMajorDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="editMajor">确 定</el-button>
			  </span>
			</el-dialog>
			<!-- 添加专业信息对话框 -->
			<el-dialog center title="添加专业信息" :visible.sync="addMajorDialogVisible" width="40%" @close="addDialogClosed" >
			  <el-form
			    :model="addMajorForm"
			    ref="addMajorForm"
			    :rules="addMajorFormRules"
			    label-width="80px">
				<el-form-item disabled label="专业名称" prop="majorname">
				  <el-input v-model="addMajorForm.majorname"></el-input>
				</el-form-item>
			    <el-form-item disabled label="所属学院" prop="college">
			      <el-select v-model="addMajorForm.college" placeholder="请选择所属学院">
			      		<el-option
							v-for="item in CollegeData"
			      			:key="item.collegename"
			      			:label="item.collegename"
			      			:value="item.collegename">
						</el-option>
			      </el-select>
			    </el-form-item>
				<el-form-item label="描述" prop="content">
					<el-input type="textarea" v-model="addMajorForm.content" />
				</el-form-item>
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="addMajorDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="addMajor()">确 定</el-button>
			  </span>
			</el-dialog>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'Major_Manage',
		data() {
			return {
				tableHeight: 100,
				query: '',
				select_college: '',
				MajorList: [],
				totle: 0,
				college: [],
				CollegeData: [],
				editMajorDialogVisible: false,
				editMajorForm: {},
				editMajorFormRules: {
					majorname: [
						{ required: true, message: '专业名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '专业名称长度为 3 ~ 10个字!', trigger: 'change' }				
					],
					college: [
						{ required: true, message: '所属学院不能为空!', trigger: 'change' },
					]
				},
				addMajorForm: {},
				addMajorDialogVisible: false,
				addMajorFormRules: {
					majorname: [
						{ required: true, message: '专业名称不能为空!', trigger: 'change' },
						{ min: 3 , max: 10 , message: '专业名称长度为 3 ~ 10个字!', trigger: 'change' }				
					],
					college: [
						{ required: true, message: '所属学院不能为空!', trigger: 'change' },
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
			this.saveNavState('/admin/majormanage')
			this.getCollegedata()
			this.getMajorList()
		},
		methods:{
			// 获取学院数据，用于条件查询
			getCollegedata() {
				this.$axios.get('admin/getcollegegroup')
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取学院信息失败！")
					}
					this.college = res.data
				})
			},
			// 清除筛选数据
			clearValues() {
				this.query = ''
				this.select_college = ''
				this.getMajorList()
				
			},
			// 添加专业 按钮点击事件
			addMajorButton() {
				this.addMajorDialogVisible = true
				// 获取学院信息
				this.$axios.get('teacher/getcollege')
				.then(res => {
					if (res.status !== 200)
						return this.$message.error("获取学院信息失败！")
					this.CollegeData = res.data
				})					
			},
			// 获取专业信息列表
			getMajorList() {
				this.$axios.post('admin/getallmajor',{
					like: this.query,
					collegename: this.select_college
				})
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取专业信息列表失败！")
					}
					this.MajorList = res.data
					this.totle = res.data.length
				})
			},
			// 添加专业信息
			addMajor() {
				console.log(this.addMajorForm)
				this.$refs.addMajorForm.validate(async valid => {
					if (valid) {
						this.$axios.post('admin/addmajor',this.addMajorForm)
						.then(res => {
							//console.log(res)
							if (res.status !== 200) {
								return this.$message.error("添加专业信息失败！")
							}
							if (res.status === 200) {
								this.addMajorDialogVisible = false
								this.$message.success("添加专业信息成功！")							
								this.getMajorList()
							}									
						})
					}
				})	
			},
			// 修改专业信息
			editMajor() {				
				this.$refs.editMajorForm.validate(async valid => {
					if(valid) {
						this.$axios.put('admin/updatemajor/' + this.editMajorForm.majorid,this.editMajorForm)
						.then(res => {
							if (res.status !== 200) {
								return this.$message.error("更新专业信息失败！")
							}
							this.editMajorDialogVisible = false
							this.$message.success("更新专业信息成功！")
							this.getMajorList()
						})
					}
				})
			},
			// 编辑专业信息  按钮点击事件
			showEditDialog(majorid) {
				this.editMajorDialogVisible = true
				this.$axios.post('admin/getmajorinfo/' + majorid)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取当前专业信息失败！")
					}
					this.editMajorForm = res.data
				})
				// 获取学院信息
				this.$axios.get('teacher/getcollege')
				.then(res => {
					if (res.status !== 200)
						return this.$message.error("获取学院信息失败！")
					this.CollegeData = res.data
				})
			},
			
			// 监听 添加专业信息 对话框关闭事件
			addDialogClosed() {
				//取消表验证
				this.$refs['addMajorForm'].resetFields();				
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath',activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			},
			// 根据专业id删除专业信息
			async removeMajorBymajorid(majorid) {
				const confirmResult = await this.$confirm('此操作将永久删除该专业信息并删除与该专业相关的所有信息，是否继续？','提示',{
					confirmButtonText: '确定',
					cancelButtonText:  '取消',
					type: 'warning'
				}).catch(err => err)
				if(confirmResult !== 'confirm') {
					return this.$message.info('已取消删除！')
				}
				this.$axios.delete('admin/deletemajor/' + majorid)
				.then(res => {
					if(res.status !== 200) {
						return this.$message.error('删除该专业信息失败！')
					}
					this.$message.success('删除该专业信息成功！')
					this.getMajorList()
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
