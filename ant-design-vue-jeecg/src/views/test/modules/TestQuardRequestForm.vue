<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="测试任务类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testType">
              <a-input v-model="model.testType" placeholder="请输入测试任务类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平台名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="platformName">
              <a-input v-model="model.platformName" placeholder="请输入平台名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="quard代码仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardRepo">
              <a-input v-model="model.quardRepo" placeholder="请输入quard代码仓库"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="quard代码分支" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quardBranch">
              <a-input v-model="model.quardBranch" placeholder="请输入quard代码分支"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="升级回滚时的当前版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="upgradeFromNum">
              <a-input v-model="model.upgradeFromNum" placeholder="请输入升级回滚时的当前版本"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="升级回滚时的升级版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="upgradeToNum">
              <a-input v-model="model.upgradeToNum" placeholder="请输入升级回滚时的升级版本"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测试用例集合" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="selectTests">
              <a-input v-model="model.selectTests" placeholder="请输入测试用例集合"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测试包链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="packageUrl">
              <a-input v-model="model.packageUrl" placeholder="请输入测试包链接"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jenkins job号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jenkinsJobNum">
              <a-input v-model="model.jenkinsJobNum" placeholder="请输入jenkins job号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jenkins job url" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jenkinsJobUrl">
              <a-input v-model="model.jenkinsJobUrl" placeholder="请输入jenkins job url"  ></a-input>
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
    name: 'TestQuardRequestForm',
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
           packageUrl: [
              { required: true, message: '请输入测试包链接!'},
           ],
           jenkinsJobNum: [
              { required: true, message: '请输入jenkins job号!'},
           ],
        },
        url: {
          add: "/test/testQuardRequest/add",
          edit: "/test/testQuardRequest/edit",
          queryById: "/test/testQuardRequest/queryById"
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