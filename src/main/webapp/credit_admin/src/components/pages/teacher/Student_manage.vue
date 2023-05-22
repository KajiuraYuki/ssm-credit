<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/teacher/welcome' }" @click.native="saveNavState('/teacher/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>学生管理</el-breadcrumb-item>
			  <el-breadcrumb-item>学生信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-row :gutter="20">			
					<el-col :span="6">
						<el-input placeholder="请输入查找内容" clearable v-model="queryInfo.query" clearable @clear="getStuListLike" >
							<el-button slot="append" icon="el-icon-search" @click="getStuListLike"></el-button>
						</el-input>					
					</el-col>					
				</el-row>
				<el-row :gutter="20" id="row1">
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_sex" placeholder="请选择性别" >
					    <el-option
					      v-for="item1 in sex"
					      :key="item1.type"
					      :label="item1.type"
					      :value="item1.type"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_college" placeholder="请选择学院" >
					    <el-option
					      v-for="item1 in college"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_major" placeholder="请选择专业" >
					    <el-option
					      v-for="item1 in major"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<el-col :span="4">
					  <!-- clearable  可以删除 -->
					  <el-select v-model="queryInfo.select_banji" placeholder="请选择班级" >
					    <el-option
					      v-for="item1 in banji"
					      :key="item1"
					      :label="item1"
					      :value="item1"
					    >
					    </el-option>
					  </el-select>
					</el-col>
					<!-- 搜索按钮 -->
					<el-col :span="2">
					  <el-button type="primary" @click="getStudentList">搜索</el-button>
					</el-col>
					<!-- 清空搜索按钮 -->
					<el-col :span="3">
					  <el-button type="info" @click="clearValues">清除搜索</el-button>
					</el-col>
				</el-row>
				<!-- 用户信息列表区域 -->
				<el-table :data="studentList" ref="table" boder stripe :height="tableHeight">
					<!-- stripe  :  斑马条纹
						 border  :  边框
					-->
					<el-table-column align="center" type="index" label="#"></el-table-column>
					<el-table-column align="center" prop="xuehao" label="学号"></el-table-column>
					<el-table-column align="center" prop="name" label="姓名"></el-table-column>
					<el-table-column align="center" prop="sex" label="性别"></el-table-column>
					<el-table-column align="center" prop="age" label="年龄"></el-table-column>
					<el-table-column align="center" prop="college" width="200px" label="学院"></el-table-column>
					<el-table-column align="center" prop="major" label="专业"></el-table-column>
					<el-table-column align="center" prop="banji" label="班级"></el-table-column>
					<el-table-column align="center" label="操作" width="100px">
						<template slot-scope="scope">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.xuehao)">编辑</el-button>							
						</template>
					</el-table-column>
				</el-table>
				<!-- 分页区域 -->
				<el-pagination
				  @size-change="handleSizeChange"
				  @current-change="handleCurrentChange"
				  :current-page="queryInfo.pagenum"
				  :page-sizes="[2, 5, 10,15]"
				  :page-size="queryInfo.pagesize"
				  layout="total, sizes, prev, pager, next, jumper"
				  :total="totle"
				></el-pagination>
			</el-card>
			<!-- 编辑角色对话框 -->
			<el-dialog center title="编辑学生信息" :visible.sync="editStudentDialogVisible" width="40%" >
			  <el-form
			    :model="editStudentForm"
			    ref="editStudentForm"
			    :rules="editStuFormRules"
			    label-width="80px">
			    <el-form-item label="学号" prop="xuehao">
			      <el-input disabled v-model="editStudentForm.xuehao"></el-input>
			    </el-form-item>
				<el-form-item disabled label="重置密码" prop="password">
				  <el-input v-model="editStudentForm.password"  placeholder="默认为使用原密码"></el-input>
				</el-form-item>
				<el-form-item label="创建时间" prop="ctime">
				  <el-input disabled v-model="editStudentForm.ctime"></el-input>
				</el-form-item>
			    <el-form-item disabled label="姓名" prop="name">
			      <el-input v-model="editStudentForm.name"></el-input>
			    </el-form-item>
				<el-form-item disabled label="年龄" prop="age">
				  <el-input v-model="editStudentForm.age"></el-input>
				</el-form-item>
				<el-form-item disabled label="性别" prop="sex">
				  <el-radio v-model="editStudentForm.sex" label="男">男</el-radio>
				  <el-radio v-model="editStudentForm.sex" label="女">女</el-radio>
				</el-form-item>				
				<el-form-item disabled label="学院" prop="college">
				  <el-input disabled v-model="editStudentForm.college"></el-input>
				</el-form-item>
				<el-form-item disabled label="专业" prop="major">
				  <el-input disabled v-model="editStudentForm.major"></el-input>
				</el-form-item>
				<el-form-item disabled label="班级" prop="banji">
				  <el-input disabled v-model="editStudentForm.banji"></el-input>
				</el-form-item>
				<el-form-item disabled label="邮箱" prop="email">
				  <el-input v-model="editStudentForm.email"></el-input>
				</el-form-item>				
			  </el-form>
			  <span slot="footer" class="dialog-footer">
			    <el-button @click="editStudentDialogVisible = false">取 消</el-button>
			    <el-button type="primary" @click="editStudent">确 定</el-button>
			  </span>
			</el-dialog>			
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'StudentManage',
		data () {
			// 自定义邮箱规则
			var checkEmail = (rule, value, callback) => {
			  const regEmail = /^\w+@\w+(\.\w+)+$/
			  if (regEmail.test(value)) {
			    // 合法邮箱
			    return callback()
			  }
			  callback(new Error('请输入合法邮箱'))
			}
			return {
				tableHeight: 100,
				queryInfo: {
					query: '',
					pagenum: 1,
					pagesize: 5,
					select_sex: '',
					select_college: '',
					select_major: '',
					select_banji: '',
				},
				studentList: [],
				sex: [{
					"id": 1,
					"type": "男"
				},								
				{
					"id": 2,
					"type": "女"
				}],
				college: [],
				major: [],
				banji: [],
				totle: 0,
				editStudentForm: {},
				editStudentDialogVisible: false,				
				editStuFormRules: {					
					password: [
						{ min: 6,max: 16,message: '密码长度为6-16位',ttigger: 'change' }
					],
					name: [
						{ required: true,message: '姓名不能为空！',trigger:'change' }
					],							
					email: [
						{ required: true,message: '邮箱不能为空！', trigger: 'change' },
						{ validator: checkEmail }
					]
				},
				password: ''
				
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
			this.saveNavState('/teacher/student')
			this.getStudentList()
			this.queryInfo.query = ''
			this.queryInfo.select_college = ''
			this.queryInfo.select_major = ''
			this.queryInfo.select_banji = ''
			this.queryInfo.select_sex = ''
		},
		methods: {				
			// 情况搜索数据
			clearValues() {
				this.queryInfo.query = '',
				this.queryInfo.select_college = '',
				this.queryInfo.select_major = '',
				this.queryInfo.select_banji = '',
				this.queryInfo.select_sex = '',
				this.getStudentList()
			},
			// 对名字进行模糊查询
			getStuListLike() {
				this.getStudentList()
			},
			// 监听 pagesize改变事件
			handleSizeChange (newSize) {
				this.queryInfo.pagesize = newSize
				this.getStudentList()
			},
			// 监听 页码值 改变事件
			handleCurrentChange (newSize) {
			  // console.log(newSize)
			  this.queryInfo.pagenum = newSize
			  this.getStudentList()()
			},
			// 获取学生信息列表函数
			getStudentList() {
				// 获取学院信息
				this.$axios.get('teacher/getcollegegroup')
				.then( res => {
					if (res.status !== 200) {
						return this.$message.error("获取学院信息失败！")
					}
					else {
						this.college = res.data	
						//console.log(res.data)
					}
				})
				
				// 获取专业信息
				this.$axios.get('teacher/getmajorgroup')
				.then( res => {
					if (res.status !== 200) {
						return this.$message.error("获取专业信息失败！")
					}
					else {
						this.major = res.data	
					}
				})
				
				// 获取班级信息
				this.$axios.get('teacher/getbanjigroup')
				.then( res => {
					if (res.status !== 200) {
						return this.$message.error("获取班级信息失败！")
					}
					else {
						this.banji = res.data	
					}
				})
				
				
				
				
				// 获取学生信息列表
				this.$axios.post('teacher/studentlist',{
					pageNum:  this.queryInfo.pagenum,
					pageSize: this.queryInfo.pagesize,
					college:  this.queryInfo.select_college,
					major:    this.queryInfo.select_major,
					banji:    this.queryInfo.select_banji,
					sex:	  this.queryInfo.select_sex,
					query:    this.queryInfo.query,
					//params: this.queryInfo
				})
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error("获取学生信息列表失败！")
					}
					this.studentList = res.data.records
					this.totle = res.data.total
					//console.log(res)
				})
			},
			// 编辑学生信息
			showEditDialog (xuehao) {
				this.$axios.get("teacher/getstu/" + xuehao)
				.then(res => {
					if (res.status !== 200)
						return this.$message.error("获取用户信息失败！")
					else
						//console.log(res.data)
						this.editStudentForm = res.data
						this.editStudentForm.password = ''
						this.password = res.data.password
						this.editStudentDialogVisible = true
				})	
			},
			// 修改学生信息
			editStudent () {
				this.$refs.editStudentForm.validate(async valid => {
					if (valid) {
						if(this.editStudentForm.password === '') {
							this.editStudentForm.password = this.password
						}
						this.$axios.put("teacher/updatestu/" + this.editStudentForm.xuehao,this.editStudentForm)
						.then(res => {
							//console.log(res)
							if (res.status !== 200 ) {
								return this.$message.error("更新学生信息失败！")
							}
							this.editStudentDialogVisible = false
							this.$message.success("更新学生信息成功！")
							this.getStudentList()
						})
					}
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
