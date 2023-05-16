<template>
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
              <a-form-model-item label="文档URL ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="documentUrlId">
                <a-input v-model="model.documentUrlId" placeholder="请输入文档URL ID"></a-input>
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
            <a-divider orientation="left">产品相关</a-divider>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品线名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productLineName">
                <j-dict-select-tag type="list" v-model="model.productLineName" dictCode="dict_bu"
                                   placeholder="请选择产品线" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productName">
                <j-dict-select-tag type="list" v-model="model.productName" dictCode="product" placeholder="请选择产品" />
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="产品经理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pmId">
                <j-select-user-by-dep v-model="model.pmId" :multi="false" />
              </a-form-model-item>
            </a-col>
            <!--Jira相关-->
            <a-divider orientation="left"  rientation-margin="50px">Jira - 同步Issue需要</a-divider>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="Jira项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectOptions">
                <j-multi-select-tag placeholder="请选择Jira项目" v-model="model.projectOptions"
                                    dictCode="pub_project,name,project_key" @change="handleJiraChange" :async="true">
                </j-multi-select-tag>
              </a-form-model-item>
            </a-col>
            <a-col :lg="8" :md="12" :sm="24" :span="24">
              <a-form-model-item label="Jira版本" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                 prop="jiraVersionName">
                <a-input v-model="model.jiraVersionName" placeholder="请输入jira版本"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <!--发布后数据-->
          <a-divider orientation="left"  rientation-margin="50px">发布后数据</a-divider>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="Commit ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="commitId">
              <a-input v-model="model.commitId" placeholder="请输入commid id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="实际发布时间" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="publishDatetime">
              <!--              <a-input v-model="model.publishDatetime" placeholder="请输入发布时间" ></a-input>-->
              <j-date v-model="model.publishDatetime" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="用户手册中文链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="urerManualEnLink">
              <a-input v-model="model.urerManualEnLink" placeholder="请输入用户手册中文链接"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :lg="12" :md="12" :sm="24" :span="24">
            <a-form-model-item label="用户手册英文链接" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userManualChLink">
              <a-input v-model="model.userManualChLink" placeholder="请输入用户手册英文链接"  ></a-input>
            </a-form-model-item>
          </a-col>

        </a-form-model>
      </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
import JMultiSelectTag from '@/components/dict/JMultiSelectTag'

export default {
  name: 'PublishlistForm',
  // mixins: [JVxeTableModelMixin],
  components: {
    JFormContainer,
    JSearchSelectTag,
    JMultiSelectTag
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
      model: {},
      dictOptions: {},
      productOptions: {},
      projectOptions: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
        lg: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      confirmLoading: false,
      validatorRules: {
        productLineName: [
          { required: true, message: '请选择产品线名!' }
        ],
        productName: [
          { required: true, message: '请选择产品名!' }
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
        documentUrlId: [
          { required: true, message: '请输入文档UrlID!' }
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
        ],
        projectOptions: [
          { required: true, message: '请选择Jira项目!' }
        ],
        jiraVersionName: [
          { required: true, message: '请输入Jira版本!' }
        ],
      },
      url: {
        add: '/release/add',
        edit: '/release/edit',
        queryById: '/release/queryById',
        publishlistProject: {
          list: '/release/publishlistProject/list',
          listByMainId: '/release/queryPublishlistProjectByMainId',
        },
        project: {
          list: "/release/project/list",
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
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add () {
      this.modelDefault.pmId = this.$store.state.user.username
      this.edit(this.modelDefault);
    },
    edit (record) {
      console.log('edit record', record)
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
        }else{
          that.$message.error("请正确填写内容")
          console.error("请正确填写内容")
        }
      })
    },
    handleJiraChange(value) {
      let selectedProject = _.split(value,',')
      getAction(this.url.project.list, {}).then((res)=> {
        if(res.success) {
          let _projectList = res.result.records || res.result
          let _selected = _.filter(_projectList, function(p) { return _.includes(selectedProject, p.projectKey)})
          this.model.publishlistProjectList = _.map(_selected, function(s) {
            return {"projectId": s.id}
          })
          this.model.projectOptions = value
          console.log('handleJiraChange value ->', value, selectedProject, _projectList, _selected, this.model.publishlistProjectList)
        }
      })
    }
  }
}
</script>

<style scoped>
</style>