<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="quard任务请求号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardRequestId">
              <a-input v-model="model.quardRequestId" placeholder="请输入quard任务请求号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jenkins job 号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jenkinsJobNum">
              <a-input v-model="model.jenkinsJobNum" placeholder="请输入jenkins job 号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="allure报告链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportLink">
              <a-input v-model="model.reportLink" placeholder="请输入allure报告链接"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalCaseNum">
              <a-input-number v-model="model.totalCaseNum" placeholder="请输入总用例数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="失败用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="failedCaseNum">
              <a-input-number v-model="model.failedCaseNum" placeholder="请输入失败用例数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="阻塞用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="brokenCaseNum">
              <a-input-number v-model="model.brokenCaseNum" placeholder="请输入阻塞用例数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="跳过用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="skippedCaseNum">
              <a-input-number v-model="model.skippedCaseNum" placeholder="请输入跳过用例数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通过用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="passedCaseNum">
              <a-input-number v-model="model.passedCaseNum" placeholder="请输入通过用例数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="未知用例数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unknownCaseNum">
              <a-input-number v-model="model.unknownCaseNum" placeholder="请输入未知用例数" style="width: 100%" />
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
    name: 'TestQuardResultForm',
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
           quardRequestId: [
              { required: true, message: '请输入quard任务请求号!'},
           ],
           jenkinsJobNum: [
              { required: true, message: '请输入jenkins job 号!'},
           ],
        },
        url: {
          add: "/test/testQuardResult/add",
          edit: "/test/testQuardResult/edit",
          queryById: "/test/testQuardResult/queryById"
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