<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="固定资产id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixAssertsId">
              <a-input v-model="model.fixAssertsId" placeholder="请输入固定资产id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原保管人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromOwnName">
              <a-input v-model="model.fromOwnName" placeholder="请输入原保管人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromDepartment">
              <a-input v-model="model.fromDepartment" placeholder="请输入原部门"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromLocation">
              <a-input v-model="model.fromLocation" placeholder="请输入原存放地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原使用状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromUseStatus">
              <a-input v-model="model.fromUseStatus" placeholder="请输入原使用状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="目的保管人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toOwnerName">
              <a-input v-model="model.toOwnerName" placeholder="请输入目的保管人员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="目的部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toDepartment">
              <a-input v-model="model.toDepartment" placeholder="请输入目的部门"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="目的存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toLocation">
              <a-input v-model="model.toLocation" placeholder="请输入目的存放地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="目的使用状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toUseStatus">
              <a-input v-model="model.toUseStatus" placeholder="请输入目的使用状态"  ></a-input>
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
    name: 'ItFixAssertsTransferRecordForm',
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
        },
        url: {
          add: "/it/itFixAssertsTransferRecord/add",
          edit: "/it/itFixAssertsTransferRecord/edit",
          queryById: "/it/itFixAssertsTransferRecord/queryById"
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