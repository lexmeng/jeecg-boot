<template>
  <a-card :bordered="false">
    <a-spin :spinning="confirmLoading">
      <j-form-container :disabled="formDisabled">
        <!-- 主表单区域 -->
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row class="form-row">
            <a-divider orientation="left">发布单</a-divider>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="发布单名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
                <a-input v-model="model.name" placeholder="请输入发布单名"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="发布版本号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="versionName">
                <a-input v-model="model.versionName" placeholder="请输入发布版本号"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="版本类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="versionType">
                <j-dict-select-tag type="list" v-model="model.versionType" dictCode="version_type"
                                   placeholder="请选择业务线" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="文档版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="documentVersion">
                <a-input v-model="model.documentVersion" placeholder="请输入文档版本"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="迭代冲刺号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scrumNum">
                <a-input v-model="model.scrumNum" placeholder="请输入迭代冲刺号"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="迭代阶段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scrumStage">
                <!--              <a-input v-model="model.scrumStage" placeholder="请输入迭代阶段" ></a-input>-->
                <j-dict-select-tag type="list" v-model="model.scrumStage" dictCode="sprint_stage"
                                   placeholder="请选择迭代阶段" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="发布单状态" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                 prop="publishlistStage">
                <j-dict-select-tag type="list" v-model="model.publishlistStage" dictCode="release_form_state"
                                   placeholder="请选择发布单状态" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="实际发布时间" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                 prop="publishDatetime">
                <!--              <a-input v-model="model.publishDatetime" placeholder="请输入发布时间" ></a-input>-->
                <j-date v-model="model.publishDatetime" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
              </a-form-model-item>
            </a-col>
            <a-divider orientation="left">产品相关</a-divider>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品线名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productLineId">
                <j-dict-select-tag type="list" v-model="model.productLineId" dictCode="dict_bu"
                                   placeholder="请选择产品线" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productId">
                <j-dict-select-tag type="list" v-model="model.productId" dictCode="product" placeholder="请选择产品" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品经理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pmId">
                <j-select-user-by-dep v-model="model.pmId" :multi="false" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="Commit ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="commitId">
                <a-input v-model="model.commitId" placeholder="请输入commid id"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="用户手册中文链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="urerManualEnLink">
                <a-input v-model="model.urerManualEnLink" placeholder="请输入用户手册中文链接"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="用户手册英文链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userManualChLink">
                <a-input v-model="model.userManualChLink" placeholder="请输入用户手册英文链接"  ></a-input>
              </a-form-model-item>
            </a-col>
            <!--Jira相关-->
            <a-divider orientation="left"  rientation-margin="50px">Jira - 同步Issue需要</a-divider>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="JRIA项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="publishlistProjectList">
                <j-search-select-tag placeholder="请选择JRIA项目" v-model="model.publishlistProjectList"
                  dict="project,name,project_key" mode="multiple"
                  :async="true">
                </j-search-select-tag>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="jira版本" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                 prop="jiraVersionName">
                <a-input v-model="model.jiraVersionName" placeholder="请输入jira版本"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

        </a-form-model>
      </j-form-container>
      <!-- 子表单区域 -->
<!--      <a-tabs v-model="activeKey" @change="handleChangeTabs">-->
<!--        <a-tab-pane tab="发布单项目表" :key="refKeys[0]" :forceRender="true">-->
<!--          <j-vxe-table-->
<!--            keep-source-->
<!--            :ref="refKeys[0]"-->
<!--            :loading="publishlistProjectTable.loading"-->
<!--            :columns="publishlistProjectTable.columns"-->
<!--            :dataSource="publishlistProjectTable.dataSource"-->
<!--            :maxHeight="300"-->
<!--            :disabled="formDisabled"-->
<!--            :rowNumber="true"-->
<!--            :rowSelection="true"-->
<!--            :toolbar="true"-->
<!--            />-->
<!--        </a-tab-pane>-->
<!--      </a-tabs>-->
    </a-spin>
  </a-card>
</template>

<script>

import { getAction } from '@/api/manage'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getRefPromise, VALIDATE_FAILED } from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import { initDictOptions } from '@comp/dict/JDictSelectUtil'
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

export default {
  name: 'PublishlistForm',
  // mixins: [JVxeTableModelMixin],
  components: {
    JFormContainer,
    JSearchSelectTag
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
        lg: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      dictOptions: {},
      productOptions: {},
      projectOptions: {},
      model: {},
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      confirmLoading: false,
      validatorRules: {
        productLineName: [
          { required: true, message: '请输入产品线名!' }
        ],
        productName: [
          { required: true, message: '请输入产品名!' }
        ],
        pmName: [
          { required: true, message: '请输入产品经理名!' }
        ],
        name: [
          { required: true, message: '请输入发布单名!' }
        ],
        versionName: [
          { required: true, message: '请输入版本名!' }
        ],
        versionType: [
          { required: true, message: '请输入版本类型!' }
        ],
        documentVersion: [
          { required: true, message: '请输入文档版本!' }
        ],
        scrumNum: [
          { required: true, message: '请输入迭代冲刺号!' }
        ],
        publishlistStage: [
          { required: true, message: '请输入发布单状态!' },
          { pattern: /^[A-Z|a-z]+$/, message: '请输入字母!' }
        ],
        pmId: [
          { required: true, message: '请输入产品经理id!' }
        ]
      },
      refKeys: ['publishlistProject'],
      tableKeys: ['publishlistProject'],
      activeKey: 'publishlistProject',
      // 发布单项目表
      publishlistProjectTable: {
        loading: false,
        dataSource: [],
        columns: [
          // {
          //   title: 'id',
          //   key: 'publishlistId',
          //   type: JVXETypes.popup,
          //   popupCode: '',
          //   field: '',
          //   orgFields: '',
          //   destFields: '',
          //   width: '200px',
          //   placeholder: '请输入${title}',
          //   defaultValue: '',
          //   validateRules: [{ required: true, message: '${title}不能为空' }]
          // },
          {
            title: 'jira project id',
            key: 'projectId',
            type: JVXETypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: ''
          },
          {
            title: 'jira项目名',
            key: 'projectName',
            type: JVXETypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          {
            title: 'jira版本名',
            key: 'jiraVersionName',
            type: JVXETypes.input,
            width: '200px',
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          }
        ]
      },
      url: {
        add: '/release/add',
        edit: '/release/edit',
        queryById: '/release/queryById',
        publishlistProject: {
          list: '/release/queryPublishlistProjectByMainId'
        }
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  created() {
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
        console.log
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
        }else{
          that.$message.error("请正确填写")
          console.error("请正确填写")
        }
      })
    },
    // addBefore() {
    //   this.publishlistProjectTable.dataSource = []
    // },
    // getAllTable() {
    //   let values = this.tableKeys.map(key => getRefPromise(this, key))
    //   return Promise.all(values)
    // },
    // /** 调用完edit()方法之后会自动调用此方法 */
    // editAfter() {
    //   this.$nextTick(() => {
    //   })
    //   // 加载子表数据
    //   if (this.model.id) {
    //     let params = { id: this.model.id }
    //     this.requestSubTableData(this.url.publishlistProject.list, params, this.publishlistProjectTable)
    //   }
    // },
    // //校验所有一对一子表表单
    // validateSubForm(allValues) {
    //   return new Promise((resolve, reject) => {
    //     Promise.all([]).then(() => {
    //       resolve(allValues)
    //     }).catch(e => {
    //       if (e.error === VALIDATE_FAILED) {
    //         // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
    //         this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
    //       } else {
    //         console.error(e)
    //       }
    //     })
    //   })
    // },
    // /** 整理成formData */
    // classifyIntoFormData(allValues) {
    //   let main = Object.assign(this.model, allValues.formValue)
    //   return {
    //     ...main, // 展开
    //     publishlistProjectList: allValues.tablesValue[0].tableData
    //   }
    // },
    // validateError(msg) {
    //   this.$message.error(msg)
    // }

  }
}
</script>

<style scoped>
</style>