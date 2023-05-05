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
            <a-form-model-item label="issue英文名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueEnName">
              <a-input v-model="model.issueEnName" placeholder="请输入issue英文名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="issue中文名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issueChName">
              <a-input v-model="model.issueChName" placeholder="请输入issue中文名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发布单id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publishlistId">
              <a-input v-model="model.publishlistId" placeholder="请输入发布单id"  ></a-input>
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
    name: 'ReleaseInfoForm',
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
           issueEnName: [
              { required: true, message: '请输入issue英文名!'},
           ],
           issueChName: [
              { required: true, message: '请输入issue中文名!'},
           ],
           publishlistId: [
              { required: true, message: '请输入发布单id!'},
           ],
        },
        url: {
          add: "/release/releaseInfo/add",
          edit: "/release/releaseInfo/edit",
          queryById: "/release/releaseInfo/queryById"
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