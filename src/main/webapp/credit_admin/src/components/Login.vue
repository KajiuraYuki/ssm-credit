
<template>
  <div id="poster">
    <el-form :model="loginForm" ref="loginForm" :rules="loginFormRules" class="login-container" label-position="left" label-width="0px">
      <h2 class="login_title">学分管理系统登录</h2>
	  <el-form-item prop="typevalue">
		  <el-select  style="width: 100%;" v-model="loginForm.typevalue" placeholder="请选择账户类型">
			  <el-option
				v-for="item in options"
				:key = "item.typevalue"
				:label="item.label"
				:value="item.typevalue"
			  ></el-option>
		  </el-select>
	  </el-form-item>
      <el-form-item prop="zhanghu">
        <el-input clearable prefix-icon="iconfont icon-denglu" type="text" v-model="loginForm.zhanghu" auto-complete="off" placeholder="账号"></el-input>
      </el-form-item>
 
      <el-form-item prop="password">
        <el-input  clearable prefix-icon="iconfont icon-denglu-mima" type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
      </el-form-item>	
 
      <el-form-item label-width="22%">
        <el-button type="info" v-on:click="login" style="width: 36%;">登录</el-button>
		<el-button type="infor" style="width: 36%;" v-on:click="forgete">忘记密码</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
 
 
<script>
	export default {
		data () {
			return {
				loginForm: {
				  zhanghu: '',
				  password: '',
				  typevalue: '',				  				  
				},
				options: [{
					typevalue: 'admin',
					label: '管理员账户',
				},{
					typevalue:'teacher',
					label:'教师账户',
				}, {
					typevalue:'student',
					label:'学生账户',
				}],
				// 表单验证
				loginFormRules: {
					// 下拉框验证
					typevalue: [
						{ required: true, message: '请选择登录类型！' ,trigger: 'change' }
					],
					zhanghu: [
						{ required: true, message: '登录账号不能为空！' ,trigger: 'change' }
					],
					password: [
						{ required: true, message: '登录密码不能为空！' ,trigger: 'change' }
					]					
				}
				}
			},	
		mounted () {
			
			this.$router.afterEach((to,from) => {
				if (this.$route.query.x === '1') {			
					this.$router.push('/login')
					this.$message.error("请先进行登录！")
				}
			})
		},
		methods: {
			
			// 忘记密码
			forgete() {
				this.$router.push('/pwd')
			},
			
			login () {	
			
			// 验证表单0
			this.$refs.loginForm.validate(async valid => {
				if (valid) {
					// 选择的账户类型为   管理员
					if (this.loginForm.typevalue === 'admin') {		  
					  this.$axios
					    .post('/login_admin', {
					      zhanghao: this.loginForm.zhanghu,
					      password: this.loginForm.password
					    },{ emulateJSON: true })
					    .then(response => {
					  	  //console.log("code is"+response.data.code)
							
						  //  状态码 200  登录成功
						  
					      if ( parseInt(response.data.code) === 200) {
							  //console.log("code is:"+response.data.code)
							 // 保存数据到客户端的 sessionStorage 中
							 window.sessionStorage.setItem('token', response.data.token)
							 window.sessionStorage.setItem('username',this.loginForm.zhanghu)
							 window.sessionStorage.setItem('pwd',this.loginForm.password)
							 window.sessionStorage.setItem('adminname',response.data.adminname)
							 // 存储当前用户类型
							 window.sessionStorage.setItem('typevalue',this.loginForm.typevalue)
							 //console.log("username:" + window.sessionStorage.getItem('username'))
							 //console.log("token:"+window.sessionStorage.getItem('token'))
					  		this.$message({
								message: response.data.msg,
								type: 'success'
							});
					        this.$router.push('/admin/welcome')
					      }
						  // 状态码  255   用户不存在
						  if (parseInt(response.data.code) === 255) {
							  this.$message.error(response.data.msg);							  
						  }
						  // 状态码  256   密码错误
						  if (parseInt(response.data.code) === 256) {
							  this.$message.error(response.data.msg);
						  }
					    })
					    .catch(error => {
					  	  this.$message.error("出现错误!错误信息：" + error)
					    })
					}
					// 选择的账户类型为   教师					
					if (this.loginForm.typevalue === 'teacher') {		  
					  this.$axios
					    .post('/login_teacher', {
					      zhanghu: this.loginForm.zhanghu,
					      password: this.loginForm.password
					    },{ emulateJSON: true })
					    .then(response => {
					  	  //console.log("code is"+response.data.code)
		
						  //  状态码 200  登录成功
						  
					      if ( parseInt(response.data.code) === 200) {
							  //console.log("code is:"+response.data.code)
							 // 保存数据到客户端的 sessionStorage 中
							 window.sessionStorage.setItem('token', response.data.token)
							 window.sessionStorage.setItem('username',this.loginForm.zhanghu)
							 window.sessionStorage.setItem('pwd',this.loginForm.password)
							 window.sessionStorage.setItem('teachername',response.data.teachername)
							 // 存储当前用户类型
							 window.sessionStorage.setItem('typevalue',this.loginForm.typevalue)
							 //console.log("username:" + window.sessionStorage.getItem('username'))
							 //console.log("token:"+window.sessionStorage.getItem('token'))
					  		this.$message({
								message: response.data.msg,
								type: 'success'
							});
					        this.$router.push('/teacher/welcome')
					      }
						  // 状态码  255   用户不存在
						  if (parseInt(response.data.code) === 255) {
							  this.$message.error(response.data.msg);							  
						  }
						  // 状态码  256   密码错误
						  if (parseInt(response.data.code) === 256) {
							  this.$message.error(response.data.msg);
						  }
					    })
					    .catch(error => {
					  	  this.$message.error("出现错误!错误信息：" + error)
					    })
					}
					// 选择的账户类型为   学生
					if (this.loginForm.typevalue === 'student') {
					  //console.log(this.loginForm.zhanghu,this.loginForm.password)
					  this.$axios
					    .post('/login_student', {
					      xuehao: this.loginForm.zhanghu,
					      password: this.loginForm.password
					    },{ emulateJSON: true })
					    .then(response => {
					  	  console.log(response.data.code)
					  	  console.log(this.value)
					      //  状态码 200  登录成功
					        if (parseInt(response.data.code) === 200) {
								window.sessionStorage.setItem('token', response.data.token)
								window.sessionStorage.setItem('username',this.loginForm.zhanghu)
								window.sessionStorage.setItem('pwd',this.loginForm.password)
								window.sessionStorage.setItem("studentname", response.data.studentname)
								// 存储当前用户类型
								window.sessionStorage.setItem('typevalue',this.loginForm.typevalue)
								this.$message({
									message: response.data.msg,
									type: 'success'
								});								
								this.$router.push('/student/welcome')
					        }
					        // 状态码  255   用户不存在
					        if (parseInt(response.data.code) === 255) {
					      	  this.$message.error(response.data.msg);							  
					        }
					        // 状态码  256   密码错误
					        if (parseInt(response.data.code) === 256) {
					      	  this.$message.error(response.data.msg);
					        }
					      })
					    .catch(error => {
					  	  this.$message.error("出现错误!错误信息：" + error)
					    })
					}
					
				}
			})			

			},			
		}
	}
</script>
 
<style>
  #poster {
	background: url(../assets/images/bj.png) ;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
	
  }
 
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 10% auto;
    width: 25%;
    padding: 35px 35px 15px 35px;
    background: #d8e3e7;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
 
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

</style>
