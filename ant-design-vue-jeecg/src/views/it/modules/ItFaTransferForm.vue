<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="固定资产编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixAssertId">
              <a-input v-model="model.fixAssertId" placeholder="请输入固定资产编号" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="新部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toDepartment">
              <j-select-depart v-model="model.toDepartment" :trigger-change="true" customReturnField="orgCode" :multi="false"></j-select-depart>
              <a-alert :message="'原部门：'+model.useOrgCode" type="info" show-icon></a-alert>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="新存放地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toLocation">
              <j-dict-select-tag type="list" v-model="model.toLocation" dictCode="it_site" placeholder="请选择存放地点" />
              <a-alert :message="'原存放地点：'+model.site_dictText" type="info" show-icon></a-alert>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="新保管人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toUserOwner">
              <j-select-multi-user v-model="model.toUserOwner" :query-config="selectUserQueryConfig"/>
              <a-alert :message="'原保管人员：'+model.useOwner" type="info" show-icon></a-alert>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="调拨状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="toUseStatus">
<!--              <a-input-number v-model="model.toUseStatus" placeholder="请输入调拨状态" style="width: 100%" />-->
              <j-dict-select-tag type="list" v-model="model.toUseStatus" dictCode="use_type" placeholder="请选择使用状态" />
              <a-alert :message="'原状态：'+model.useState_dictText" type="info" show-icon></a-alert>
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
    name: 'ItFaForm',
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
        // 选择用户查询条件配置
        selectUserQueryConfig: [
          {key: 'email', label: '邮箱'},
        ],
        url: {
          transfer: "/it/itFa/transfer",

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
        console.log('edit', record)

        this.model = Object.assign({}, record);
        this.model.fixAssertId = record.fixedAssetsNumber

        this.visible = true;
        console.log('edit', this.model)
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            const formData = new FormData();
            formData.append('fixAssertId', this.model.fixedAssetsNumber)
            formData.append('toDepartment', this.model.toDepartment)
            formData.append('toLocation', this.model.toLocation)
            formData.append('toUserOwner', this.model.toUserOwner)
            formData.append('toUseStatus', this.model.toUseStatus)
            // let postData = {
            //   fixAssertId: this.model.fixedAssetsNumber,
            //   toDepartment: this.model.toDepartment,
            //   toLocation: this.model.toLocation,
            //   toUserOwner: this.model.toUserOwner,
            //   toUseStatus: this.model.toUseStatus
            // }
            console.log("formData", formData)
            httpAction(this.url.transfer,formData,'post').then((res)=>{
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