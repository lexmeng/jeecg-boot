<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="issue号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueNum">
              <a-input v-model="model.issueNum" placeholder="请输入issue号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueName">
              <a-input v-model="model.issueName" placeholder="请输入issue名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueType">
              <a-input v-model="model.issueType" placeholder="请输入issue类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueLink">
              <a-input v-model="model.issueLink" placeholder="请输入issue链接"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发布单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publishlistId">
              <a-input v-model="model.publishlistId" placeholder="请输入发布单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectId">
              <a-input v-model="model.projectId" placeholder="请输入项目名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jira版本名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiraVersionName">
              <a-input v-model="model.jiraVersionName" placeholder="请输入jira版本名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当初创建的日期时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueCreateDatetime">
              <j-date placeholder="请选择当初创建的日期时间"  v-model="model.issueCreateDatetime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'IssueHistoryForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           issueNum: [
              { required: true, message: '请输入issue号!'},
           ],
           issueName: [
              { required: true, message: '请输入issue名!'},
           ],
           issueType: [
              { required: true, message: '请输入issue类型!'},
           ],
           issueLink: [
              { required: true, message: '请输入issue链接!'},
           ],
           publishlistId: [
              { required: true, message: '请输入发布单号!'},
           ],
           projectId: [
              { required: true, message: '请输入项目名!'},
           ],
           jiraVersionName: [
              { required: true, message: '请输入jira版本名!'},
           ],
           issueCreateDatetime: [
              { required: true, message: '请输入当初创建的日期时间!'},
           ],
        },
        url: {
          add: "/release/issueHistory/add",
          edit: "/release/issueHistory/edit",
          queryById: "/release/issueHistory/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
    }
  }
</script>