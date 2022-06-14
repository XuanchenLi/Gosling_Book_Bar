<template>
  <div class="search-employee">
    <el-container class="department-box">
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>员工管理</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="search-box">
        <el-row class="searcher" justify="space-evenly">
          <el-col :span="5">
            <div style="float: right">
              <el-select v-model="typeSelect" placeholder="Select" style="width: 110px">
                <el-option label="全部员工" value="0" />
                <el-option label="在职员工" value="1" />
                <el-option label="离职员工" value="2" />
              </el-select>
            </div>
          </el-col>
          <el-col :span="13">
            <el-autocomplete
              v-model="searchInput"
              :fetch-suggestions="getRecommend"
              clearable
              @select="handleSelect"
              @keyup.enter.native="requestData"
              style="width: 505px; margin-left: 6%"
              :trigger-on-focus="false"
            >
              <template #prepend>
                <el-select v-model="regionSelect" placeholder="Select" style="width: 100px">
                  <el-option label="账户名" value="0" />
                  <el-option label="编号" value="1" />
                </el-select>
              </template>
              <template #append>
                <el-button @click="requestData" style="width: 50px; padding: 10px">
                  <el-icon><Search/></el-icon>
                </el-button>
              </template>
            </el-autocomplete>
          </el-col>
        </el-row>

        <el-row class="time-block row-bg" justify="space-evenly">
          <el-col :span="5">
            <span style="line-height: 40px; float: right; ">入职时间:</span>
          </el-col>
          <el-col :span="6" style="margin-left: 2%">
            <div class="start-block">
              <el-date-picker
                v-model="startDate"
                type="date"
                placeholder="Pick a day"
                :shortcuts="shortcuts"
              />
            </div>
          </el-col>
          <el-col :span="1">
            <span style="line-height: 40px;">——</span>
          </el-col>
          <el-col :span="6">
            <div class="end-block">
              <el-date-picker
                v-model="endDate"
                type="date"
                placeholder="Pick a day"
                :shortcuts="shortcuts"
              />
            </div>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="clearDate">
              清空
            </el-button>
          </el-col>
        </el-row>
      </el-main>
      <el-main class="main" ref="mainWindow" v-loading="this.mainLoading">
        <el-table
          class="main-table"
          :data="tableData"
          style="margin-bottom: 20px"
          row-key="id"
          stripe
          border
          default-expand-all
        >
          <el-table-column prop="id" align="center" label="编号"  width="70" />
          <el-table-column prop="name" align="center" label="姓名"  />
          <el-table-column prop="sex" align="center" label="性别"  />
          <el-table-column prop="startDate" align="center" label="入职日期"  />
          <el-table-column prop="endDate" align="center" label="离职日期"  />
          <el-table-column align="center" label="操作" width="200px" >
            <template #default="scope">
              <el-row style="margin-bottom: 5px">
              <el-button size="small" type="warning" :disabled="this.tableData[scope.$index].status !== 0"
                         @click="allocate(scope.$index, $event)">岗位调度</el-button>
              <el-button size="small" type="info" :disabled="this.tableData[scope.$index].status !== 0"
                         @click="setSalary(scope.$index, $event)">薪资设置</el-button>
              </el-row>
              <el-row>
              <el-button size="small" type="primary" @click="viewInfo(scope.$index, $event)">查看信息</el-button>
              <el-button size="small" type="success" @click="controlAccount(scope.$index, $event)">账号控制</el-button>
              </el-row>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-block">
          <el-pagination
            v-model:currentPage="currentPage"
            v-model:page-size="pageSize"
            :background=true
            layout="prev, pager, next, jumper"
            :total="totalSize"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-main>
    </el-container>
    <!--  -->
    <el-dialog v-model="openAllocateDialog" title="岗位调度">
      <el-row style="margin-bottom: 20px">
        <el-col  :span="12" class="info" >
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.id}}</h3>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-card v-loading="allocateLoad" class="position-card">
        <template #header>
          <el-row class="card-header">
            <el-col :span="3">
              <span>所在岗位</span>
            </el-col>
            <el-col :span="7">
              <el-select v-model="department" @change="fillPosition"  placeholder="Select">
                <el-option
                  v-for="item in departments"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-col>
            <el-col :span="7" style="margin-left: 10px">
              <el-select v-model="position"  placeholder="Select" >
                <el-option
                  v-for="item in positions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button class="button" size="small"
                         type="success" text
                         @click="addPosition"
              >
                添加岗位</el-button>
            </el-col>
          </el-row>
        </template>
        <el-table :data="departmentPositionList"
                  height="250"
                  stripe
                  max-height="400px"
                  style="width: 100%">
          <el-table-column prop="departmentName" align="center" label="部门" />
          <el-table-column prop="positionName" align="center" label="岗位"  />
          <el-table-column align="center" label="" width="100" >
            <template #default="scope">
              <el-button style="width: 32px; height: 32px;padding: 0"
                         size="small" type="danger"
                         @click="deleteEmployeePosition(scope.$index, $event)"
              >
                <el-icon><CloseBold/></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

    </el-dialog>
    <!--  -->
    <el-dialog
               v-model="openInfoDialog"
               title="查看员工信息"
    >
      <div v-loading="this.infoLoad">
      <el-row>
        <el-col :span="6" :offset="2">
          <el-avatar :size="150" :src="this.baseUrl + this.avatar">
            <template  #default>
              <el-icon style="font-size: 80px; padding-top: 30px" ><UserFilled/></el-icon>
            </template>
          </el-avatar>
        </el-col>
        <el-col style="padding-left: 20px" :span="12" class="info" :offset="2">
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.id}}</h3>
            </el-row>
            <el-row>
              <h3 >邮箱：{{this.email}}</h3>
            </el-row>
            <el-row>
              <h3 >手机号：{{this.phone}}</h3>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-row class="info-time" justify="center">
        <el-col :span="8">
          <h3 >在职时间：&nbsp;&nbsp;{{this.mStartDate}}</h3>
        </el-col>
        <el-col :span="2">
          <h3 >——</h3>
        </el-col>
        <el-col :span="8">
          <h3 style="text-align: left">&nbsp;&nbsp;{{ this.mEndDate }}</h3>
        </el-col>
      </el-row>

      <el-row class="job-row">
        <el-descriptions
          class="margin-top"
          title="所在部门岗位"
          :column="2"
          :size="10"
          border
          align="center"
        >
          <div v-if="departmentPositionList.length === 0">
            <el-descriptions-item >
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <OfficeBuilding />
                  </el-icon>
                  部门
                </div>
              </template>
              暂无数据
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <Suitcase />
                  </el-icon>
                  岗位
                </div>
              </template>
              暂无数据
            </el-descriptions-item>
          </div>
          <div v-for="(value, index) in departmentPositionList" :key="index">
            <el-descriptions-item >
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <OfficeBuilding />
                  </el-icon>
                  部门
                </div>
              </template>
              {{value.departmentName}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <Suitcase />
                  </el-icon>
                  岗位
                </div>
              </template>
              {{value.positionName}}
            </el-descriptions-item>
          </div>
        </el-descriptions>
      </el-row>
      </div>
      <el-row>
        <el-col :span="24">
          <h2 style="float:left;"> 岗位调度历史 </h2>
        </el-col>
      </el-row>
      <el-row style="width: 100%;">
        <el-col :span="24">
          <hr style="color:#42b983"/>
        </el-col>
      </el-row>
      <el-row>

        <el-col :span="24">
          <el-scrollbar height="200px">
            <el-timeline  >
              <el-timeline-item
                v-for="(activity, index) in history"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :timestamp="activity.timestamp"
                style="text-align: left;"
              >
                <div style="float: left; width: 100%; text-align: left">{{ activity.content }}</div>
              </el-timeline-item>
            </el-timeline>
          </el-scrollbar>
        </el-col>
      </el-row>

    </el-dialog>
    <!-- -->
    <el-dialog v-model="openACDialog" title="账号控制">
      <el-row style="margin-bottom: 20px">
        <el-col  :span="12" class="info" >
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.id}}</h3>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <el-row>
            <div style="height: 40px"></div>
          </el-row>
          <el-row>
            <el-col :span="24" style="margin-right: 0">
              <el-button type="danger" style="width: 100px;height: 30px; float: right">
                <span style="font-size: 15px">删除账号</span>
              </el-button>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-card v-loading="ACLoad" class="ac-card">
        <template #header>
          <el-row class="card-header">
            <el-col :span="3">
              <span style="line-height: 40px">账号角色</span>
            </el-col>
            <el-col :span="7" :offset="5">
              <el-select v-model="role"  placeholder="Select">
                <el-option
                  v-for="item in roles"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                />
              </el-select>
            </el-col>
            <el-col :span="4" :offset="4">
              <el-button class="button" size="small"
                         type="success"
                         @click="addRole($event)"
                         style="width: 32px;height: 32px; padding: 0;font-size: 26px"
              >
                <el-icon><CirclePlus/></el-icon>
              </el-button>
            </el-col>
          </el-row>
        </template>
        <el-table :data="accountRoleList"
                  height="250"
                  stripe
                  max-height="400px"
                  style="width: 100%">
          <el-table-column prop="id" align="center" label="编号" />
          <el-table-column prop="name" align="center" label="角色"  />
          <el-table-column align="center" label="" width="100" >
            <template #default="scope">
              <el-button style="width: 32px; height: 32px;padding: 0;font-size: 26px"
                         size="small" type="danger"
                         round
                         @click="deleteEmployeeRole(scope.$index, $event)"
              >
                <el-icon><CircleClose/></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

    </el-dialog>
    <el-dialog v-model="openSalaryDailog" title="薪资设置" width="70%">
      <el-row style="margin-bottom: 20px">
        <el-col  :span="12" class="info" >
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.salaryForm.employeeId}}</h3>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-form v-loading="salaryLoad" :model="salaryForm" :rules="salaryRules" ref="salaryForm">
        <el-descriptions
          title="当前薪资"
          :column="6"
          border
          direction="vertical"
        >
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                基础工资
              </div>
            </template>
            <el-form-item prop="basePay">
              <el-input-number  v-model="salaryForm.basePay"
                                :min="0" :max="9999999" :precision="2"
                                autocomplete="off"
                                size="small"
                                controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                绩效奖金
              </div>
            </template>
            <el-form-item prop="meritPay">
              <el-input-number v-model="salaryForm.meritPay" :min="0"
                               :max="9999999"
                               :precision="2" autocomplete="off"
                               size="small"
                               controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-descriptions-item >
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                补贴
              </div>
            </template>
            <el-form-item prop="subsidyPay" label-align="right" align="center">
              <el-input-number v-model="salaryForm.subsidyPay" :min="0"
                               :max="9999999"
                               :precision="2" autocomplete="off"
                               size="small"
                               controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                加班费
              </div>
            </template>
            <el-form-item prop="overtimePay">
              <el-input-number v-model="salaryForm.overtimePay" :min="0"
                               :max="9999999" :precision="2"
                               autocomplete="off"
                               size="small"
                               controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                保险
              </div>
            </template>
            <el-form-item prop="insurancePay">
              <el-input-number v-model="salaryForm.insurancePay" :min="-9999999"
                               :max="0" :precision="2"
                               autocomplete="off"
                               size="small"
                               controls-position="right"
              ></el-input-number>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label-align="right" align="center">
            <template #label>
              <div class="cell-item">
                实发工资
              </div>
            </template>
            <span style="line-height: 40px">{{this.salaryForm.basePay + this.salaryForm.insurancePay + this.salaryForm.meritPay
            + this.salaryForm.overtimePay + this.salaryForm.subsidyPay }}</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="submitForm('salaryForm')">确定</el-button>
        <el-button @click="closeForm('salaryForm')">返回</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import HelloWorld from '@/components/HelloWorld'

import { Search, UserFilled } from '@element-plus/icons'
import { requestEmployeeDateAPI, getEmployeeRecommend } from '@/api/search'
import { Picture, Avatar, MoreFilled, OfficeBuilding, Warning,
  Suitcase, CloseBold, CirclePlus, CircleClose } from '@element-plus/icons'
import { getEmployeeInfoAPI, getEmployeePositionAPI,
  deleteEmployeePositionAPI, addEmployeePositionAPI, getEmployeeAllocateHistory } from '@/api/employee'
import { getAllBasicAPI } from '@/api/department'
import { getAllBasicByDepartmentIdAPI } from '@/api/position'
import { getAllRoleAPI, getEmployeeRoleAPI, addEmployeeRoleAPI, removeEmployeeRoleAPI } from '@/api/role'
import { getSalaryByEmployeeIdAPI, setSalaryAPI } from '@/api/salary'

export default {
  name: 'SearchEmployeeView',
  components: {
    UserFilled,
    Search,
    Picture,
    Avatar,
    MoreFilled,
    OfficeBuilding,
    Suitcase,
    CloseBold,
    CirclePlus,
    CircleClose,
    Warning
  },
  data () {
    return {
      baseUrl: "http://127.0.0.1:8088/api/",
      openSalaryDailog: false,
      salaryLoad: false,
      ACLoad: false,
      openACDialog: false,
      openAllocateDialog: false,
      allocateLoad: false,
      openInfoDialog: false,
      infoLoad: false,
      currentPage: 1,
      pageSize: 10,
      totalSize: 1,
      searchInput: "",
      mainLoading: false,
      typeSelect: "0",
      regionSelect: "0",
      tableData: [],
      startDate: null,
      endDate: null,
      shortcuts: [
        {
          text: 'Today',
          value: new Date(),
        },
        {
          text: 'Yesterday',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          },
        },
        {
          text: 'A week ago',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          },
        },
      ],
      //个人信息
      username: "",
      sex: "",
      id: 0,
      mStartDate: null,
      mEndDate: null,
      phone: "",
      email: "",
      avatar: "",
      history: [],
      departmentPositionList: [],
      //调度信息
      department: null,
      position: null,
      departments: [],
      positions: [],
      //账号控制信息
      roles: [],
      role: null,
      accountRoleList: [],
      salaryForm: {
        id: null,
        employeeId: 0,
        basePay: 0,
        insurancePay: 0,
        meritPay: 0,
        subsidyPay: 0,
        overtimePay: 0,
      },
      salaryRules: {
        basePay: [
          { type: 'number', require: true, trigger: 'blur' }
        ],
        insurancePay: [
          { type: 'number', require: true, trigger: 'blur' }
        ],
        meritPay: [
          { type: 'number', require: true, trigger: 'blur' }
        ],
        subsidyPay: [
          { type: 'number', require: true, trigger: 'blur' }
        ],
        overtimePay: [
          { type: 'number', require: true, trigger: 'blur' }
        ]
      },

    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName === "salaryForm") {
            setSalaryAPI(this.salaryForm).then(
              (res) => {
                if (res.data.success) {
                  this.$message.success("设置成功")
                } else {
                  this.$message.error(res.data.message)
                }
              }
            ).catch(
              (err) => {
                this.$message.error(err.message)
              }
            )
          }
        }
      })
    },
    resetSalary() {
      this.salaryForm.id = null
      this.salaryForm.basePay = 0
      this.salaryForm.employeeId = 0
      this.salaryForm.insurancePay = 0
      this.salaryForm.overtimePay = 0
      this.salaryForm.meritPay = 0
      this.salaryForm.subsidyPay = 0
    },
    closeForm(formName) {
      if (formName === "salaryForm") {
        this.resetSalary()
        this.openSalaryDailog = false
      }
    },
    deleteEmployeePosition(index, event) {
      this.myBlur(event)
      let position = this.departmentPositionList[index]
      deleteEmployeePositionAPI(this.id, position.positionId).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList.splice(index, 1)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    }
    ,
    clearInfo() {
      this.username = ""
      this.sex = ""
      this.id = 0
      this.mStartDate = null
      this.mEndDate = null
      this.phone = ""
      this.email = ""
      this.departmentPositionList = []
      this.history = []
      this.avatar = ""
    },
    setInfo(profile) {
      this.username = profile.username
      this.sex = profile.sex === 0 ? "男" : "女"
      this.id = profile.id
      this.mStartDate = profile.startDate
      this.mEndDate = profile.endDate
      this.phone = profile.phone
      this.email = profile.email
      this.avatar = profile.avatar
    }
    ,
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    setRoleDisabledStatus(id, p) {
      for (let index in this.roles) {
        if (this.roles[index].value === id) {
          this.roles[index].disabled = p
          return;
        }
      }
    },
    getRole(id) {
      for (let index in this.roles) {
        if (this.roles[index].value === id) {
          return this.roles[index]
        }
      }
      return null
    },
    addRole(event) {
      this.myBlur(event)
      addEmployeeRoleAPI(this.id, this.role).then (
        (res) => {
          if(res.data.success) {
            let r = this.getRole(this.role)
            this.accountRoleList.push(
              {
                id: r.value,
                name: r.label,
                key: r.key
              }
            )
            this.setRoleDisabledStatus(this.role, true)
            this.role = null
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    deleteEmployeeRole(index, event) {
      this.myBlur(event)
      let r = this.accountRoleList[index]
      removeEmployeeRoleAPI(this.id, r.id).then (
        (res) => {
          if(res.data.success) {
            this.setRoleDisabledStatus(r.id, false)
            this.accountRoleList.splice(index, 1)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    isExistRole(id) {
      for (let index in this.accountRoleList) {
        if (this.accountRoleList[index].id === id)
          return true;
      }
      return false;
    },
    controlAccount(index, event) {
      this.myBlur(event)
      this.openACDialog = true
      this.ACLoad = true
      this.clearInfo()
      this.roles = []
      this.accountRoleList = []
      this.role = null
      let data = this.tableData[index]
      this.id = data.id
      this.username = data.name
      getEmployeeRoleAPI(this.id).then(
        (res) => {
          if (res.data.success) {
            console.log(res.data.data)
            this.accountRoleList = res.data.data
            getAllRoleAPI().then(
              (res) => {
                if (res.data.success) {
                  let data = res.data.data
                  for (let index in data) {
                    this.roles.push(
                      {
                        value: data[index].id,
                        label: data[index].name,
                        key: data[index].key,
                        disabled: this.isExistRole(data[index].id)
                      }
                    )
                  }
                  this.ACLoad = false
                }else {
                  this.$message.error(res.data.message)
                }
              }
            )
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    setSalary(index, event) {
      this.myBlur(event)
      this.openSalaryDailog = true
      this.salaryLoad = true
      this.clearInfo()
      let data2 = this.tableData[index]
      this.salaryForm.employeeId = data2.id
      this.username = data2.name
      getSalaryByEmployeeIdAPI(this.salaryForm.employeeId).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            if (data !== null) {
              //console.log(data)
              this.salaryForm.id = data['id']
              this.salaryForm.overtimePay = data['overtimePay']
              this.salaryForm.basePay = data['basePay']
              this.salaryForm.meritPay = data['meritPay']
              this.salaryForm.subsidyPay = data['subsidyPay']
              this.salaryForm.insurancePay = data['insurancePay']
            }else {
              this.resetSalary()
              this.salaryForm.employeeId = data2.id
            }
            this.salaryLoad = false
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },

    allocate(index, event) {
      this.myBlur(event)
      this.department = null
      this.position = null
      this.openAllocateDialog = true
      this.allocateLoad = true
      this.clearInfo()
      let data = this.tableData[index]
      this.id = data.id
      this.username = data.name
      getEmployeePositionAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getAllBasicAPI().then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.departments = []
            for (let index in data) {
              this.departments.push(
                {
                  value: data[index].id,
                  label: data[index].name
                }
              )
            }
            this.allocateLoad = false
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    isExist(id) {
      for (let index in this.departmentPositionList) {
        if (id === this.departmentPositionList[index].positionId)
          return true
      }
      return false
    }
    ,
    fillPosition() {
      this.positions = []
      this.position = null
      getAllBasicByDepartmentIdAPI(this.department).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.positions = []
            for (let index in data) {
              this.positions.push(
                {
                  value: data[index].id,
                  label: data[index].name,
                  disabled: this.isExist(data[index].id)
                }
              )
            }
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    getDepartmentName(id) {
      for (let index in this.departments) {
        if (this.departments[index].value === id)
          return this.departments[index].label
      }
      return ""
    },
    getPositionName(id) {
      for (let index in this.positions) {
        if (this.positions[index].value === id)
          return this.positions[index].label
      }
      return ""
    },
    addPosition (event) {
      this.myBlur(event)
      addEmployeePositionAPI(this.id, this.position).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList.push(
              {
                positionId: this.position,
                departmentId: this.department,
                positionName: this.getDepartmentName(this.department),
                departmentName: this.getPositionName(this.position),
              }
            )
            this.position = null
            this.department =null
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    viewInfo(index, event) {
      this.myBlur(event)
      this.openInfoDialog = true
      this.infoLoad = true
      this.clearInfo()
      let data = this.tableData[index]
      getEmployeeInfoAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            let profile = res.data.data
            this.setInfo(profile)
            // TODO 加载头像
            console.log(profile)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getEmployeePositionAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getEmployeeAllocateHistory(data.id).then(
        (res) => {
          if (res.data.success) {
            console.log(res.data.data)
            let data = res.data.data
            this.history = []
            for (let index in data) {
              this.history.push(
                {
                  timestamp: data[index].allocateDate,
                  content: (data[index].type === 0 ? "+ " : "- ") +
                    data[index].departmentName + " - " + data[index].positionName,
                  color: (data[index].type === 0 ? "#2E769F" : "#9B312E")
                }
              )
            }
            this.infoLoad = false
          }else {
            this.$message.error(res.data.message)
          }
        }
      )
        .catch(
          (err) => {
            this.$message.error(err.message)
          }
        )
    }
    ,
    requestData() {
      const searchBag = {
        currentPage: this.currentPage === "undefined" ? 1 : this.currentPage,
        regionSelect: parseInt(this.regionSelect),
        typeSelect: parseInt(this.typeSelect),
        startDate: this.startDate,
        endDate: this.endDate,
        query: this.searchInput
      }
      // console.log(searchBag)
      this.mainLoading = true
      requestEmployeeDateAPI(searchBag).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.currentPage = data.currentPage
            this.totalSize = data.totalNum
            this.mainLoading = false
            this.tableData = []
            //console.log(data)
            for (let i = 0; i < data.employeeList.length; ++i){
              this.tableData.push(
                {
                  id: data.employeeList[i].id,
                  name: data.employeeList[i].username,
                  startDate: data.employeeList[i].startDate,
                  endDate: data.employeeList[i].endDate,
                  sex: data.employeeList[i].sex === 0 ? "男" : "女",
                  status: data.employeeList[i].status
                }
              )
            }
            // console.log(res.data.data)
          }else {
            this.$message.error(res.data.message)
            this.mainLoading = false
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
          this.mainLoading = false
        }
      )
    },
    clearDate() {
      this.startDate = null
      this.endDate = null
    },
    handleSizeChange(val) {
      console.log(`${val} items per page`)
    },
    handleCurrentChange(val) {
      this.requestData()
    },
    getRecommend(queryString, cb){

      let results = []
      console.log(this.regionSelect)
      if (parseInt(this.regionSelect) === 1) {
        cb(results)
        return
      }
      if (this.regionSelect === 1 || this.searchInput === "" || this.searchInput === null)
        cb(results)
      const searchBag = {
        currentPage: this.currentPage === "undefined" ? 1 : this.currentPage,
        regionSelect: parseInt(this.regionSelect),
        typeSelect: parseInt(this.typeSelect),
        startDate: this.startDate,
        endDate: this.endDate,
        query: this.searchInput
      }
      getEmployeeRecommend(searchBag).then(
        (res) => {
          var timeout
          if (res.data.success) {
            let data = res.data.data
            for (let i = 0; i < data.length; ++i) {
              results.push({ value: data[i], address: ""})
            }
            //console.log(results)
            results = queryString
              ? results.filter(this.createFilter(queryString))
              : results;
            clearTimeout(timeout)
            timeout = setTimeout(
              () => {
                cb(results)
              }, 2000)
            // console.log(res.data.data)
            cb(results)
          } else {
            cb(results)
          }
        }
      ).catch(
        (err) => {
          cb(results)
        }
      )
      // call callback function to return suggestions
      cb(results)
    }
    ,
    handleSelect(item) {
      this.requestData()
    },
    createFilter(queryString) {
      return (item) => {
        return item.value.toUpperCase().match(queryString.toUpperCase());
      };
    },
  },
  created () {
  }
  ,
  mounted () {
    this.requestData()
  }
}
</script>

<style scoped>
.search-employee {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: auto;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}
.sub-header {
  background-color: white;
  height: 40px;
}
.search-box {
  width: 90%;
  margin-left: 5%;
  margin-top: 1%;
  overflow: hidden;
}

.main {
  margin-top: 1%;
}
.main-table {
  width: 90%;
  margin-left: 5%;
  height: 990px;
}
.pagination-block + .pagination-block {
  margin-top: 10px;
}
.time-block {
}

.search-box .searcher {

}
.time-block {
  margin-top: 1%;
}
.avatar .el-image {
  padding: 0 5px;
  width: 150px;
  height: 150px;
  border: #a1b0b9 solid 1px;
}
.avatar .image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  color: var(--el-text-color-secondary);
  font-size: 30px;
}
.avatar .image-slot .el-icon {
  font-size: 30px;
}

.info .el-row{
  height: 40px;
}
.info h2 {
  margin-top: 5px;
  margin-left: 0;
}
.info h3 {
  margin-top: 5px;
  margin-left: 0;
}
hr { display: block; height: 1px;

  border: 0; border-top: 1px solid #ccc;

  margin: 1em 0; padding: 0; }

.el-descriptions {
  margin-top: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
.position-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.position-card .text {
  font-size: 14px;
}

.position-card .item {
  margin-bottom: 18px;
}


</style>
