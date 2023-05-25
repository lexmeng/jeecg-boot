<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="发布单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publishlistId">
              <span class="ant-form-text">{{ model.publishlistId }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jira版本名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiraVersionName">
              <span class="ant-form-text">{{ model.jiraVersionName }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectId">
              <span class="ant-form-text">{{ model.projectId }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueNum">
              <span class="ant-form-text">{{ model.issueNum }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueType">
              <span class="ant-form-text">{{ model.issueType }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueLink">
              <span class="ant-form-text">{{ model.issueLink }}</span>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueName">
              <a-textarea v-model="model.issueName" placeholder="请输入issue名" :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中文名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueChName">
              <a-textarea v-model="model.issueChName" placeholder="请输入中文名" :auto-size="{ minRows: 3, maxRows: 5 }" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="英文名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueEnName">
              <a-textarea v-model="model.issueEnName" placeholder="请输入英文名" :auto-size="{ minRows: 3, maxRows: 5 }" />
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
    name: 'IssueForm',
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
           issueChName: [
              { required: true, message: '请输入中文名!'},
           ],
           issueEnName: [
              { required: true, message: '请输入英文名!'},
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
              { required: true, message: '请输入项目id!'},
           ],
           jiraVersionName: [
              { required: true, message: '请输入jira版本名!'},
           ],
        },
        url: {
          add: "/release/issue/add",
          edit: "/release/issue/edit",
          queryById: "/release/issue/queryById"
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