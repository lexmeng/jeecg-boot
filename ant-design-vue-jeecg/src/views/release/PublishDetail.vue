<template>
  <page-layout :title="'发布单：' + publishForm.name"
    logo="https://gw.alipayobjects.com/zos/rmsportal/nxkuOJlFJuAUhzlMTCEe.png">
    <detail-list slot="headerContent" size="small" :col="3" class="detail-layout">
      <detail-list-item term="创建人">{{ publishForm.createBy }}</detail-list-item>
      <detail-list-item term="产品线">{{ publishForm.productLineName }}</detail-list-item>
      <detail-list-item term="创建时间">{{ publishForm.createTime }}</detail-list-item>
      <detail-list-item term="产品">{{ publishForm.productName }}</detail-list-item>
      <detail-list-item term="迭代冲刺号">{{ publishForm.scrumNum }}</detail-list-item>
      <detail-list-item term="版本类型">{{ publishForm.versionType }}</detail-list-item>
      <detail-list-item term="版本">{{ publishForm.versionName }}</detail-list-item>
      <detail-list-item term="JIRA版本">{{ publishForm.jiraVersionName }}</detail-list-item>
    </detail-list>
    <a-row slot="extra" class="status-list">
      <a-col :xs="12" :sm="12">
        <div class="text">发布状态</div>
        <div class="heading">{{ publishForm.publishlistStage }}</div>
      </a-col>
      <a-col :xs="12" :sm="12">
        <div class="text">版本类型</div>
        <div class="heading">{{ publishForm.versionType }}</div>
      </a-col>
    </a-row>
    <!-- actions -->
    <template slot="action">
      <a-button type="dashed" icon="reload" @click="reloadData"></a-button>
      <a-button type="primary" style="margin:0 4px" icon="cloud-upload" @click="publish">发布</a-button>
      <a-button-group>
        <a-button @click="handleReleaseNote">ReleaseNote</a-button>
        <a-button @click="handleReleaseMail">ReleaseMail</a-button>
        <a-button @click="handlePackagePR">产品包 PR 文本</a-button>
        <a-button @click="handleHandbookChPR">手册中文 PR 文本</a-button>
        <a-button @click="handleHandbookEnPR">手册英文 PR 文本</a-button>
        <a-button @click="handleWebsite">官网文本</a-button>
      </a-button-group>
    </template>

    <!--    <a-card :bordered="false" title="Sprint 迭代进度">-->
    <!--      <a-steps :direction="isMobile() && 'vertical' || 'horizontal'" :current="sprintCurrent" progressDot>-->
    <!--        <a-step v-for="item in sprintStages" :key="item.key" :title="item.text"></a-step>-->
    <!--      </a-steps>-->
    <!--    </a-card>-->
    <a-collapse :default-active-key="currentStage" :bordered="false">
      <template #expandIcon="props">
        <a-icon type="caret-right" :rotate="props.isActive ? 90 : 0" />
      </template>
      <a-collapse-panel key="1" header="研发阶段" :style="customStyle">
        <issue-dev-list :pid="publishlistId" ref="issueList"></issue-dev-list>
      </a-collapse-panel>
      <a-collapse-panel key="2" header="测试阶段" :style="customStyle">
        <a-button-group>
          <a-button @click="handlePakcage" type="primary" icon="build">打QA包</a-button>
          <a-button @click="handleQuard" type="primary" icon="build">Run Quard</a-button>
          <a-button @click="handleStep" type="primary" icon="build">Run Step</a-button>
        </a-button-group>

        <!--        <a-steps direction="horizontal" :current="testingCurrent" progressDot>-->
        <!--          <a-step key="1" title="打包"></a-step>-->
        <!--          <a-step key="2" title="Quard"></a-step>-->
        <!--          <a-step key="3" title="Step"></a-step>-->
        <!--        </a-steps>-->
      </a-collapse-panel>
      <a-collapse-panel key="3" header="交付阶段" :style="customStyle">

      </a-collapse-panel>
    </a-collapse>

    <gen-text-drawer ref="modalForm" @ok="modalFormOk"></gen-text-drawer>

    <!--    <a-card class="card" style="margin-top: 24px" :title="txtTitle" :bordered="false">-->
    <!--      <j-markdown-editor v-if="isMarkDown" v-model="txtContent"></j-markdown-editor>-->
    <!--      <j-editor v-else v-model="txtContent"></j-editor>-->
    <!--    </a-card>-->
  </page-layout>
</template>

<script>
import JMarkdownEditor from '@comp/jeecg/JMarkdownEditor/index'
import PageLayout from '@comp/page/PageLayout'
import DetailList from '@comp/tools/DetailList'
import { mixinDevice } from '@/utils/mixin'
import PublishlistForm from './modules/PublishlistForm'
import { initDictOptions } from '@comp/dict/JDictSelectUtil'
import { getAction } from '@api/manage'
import GenTextDrawer from "@views/release/modules/GenTextDrawer.vue";
import ProjectDrawer from "@views/release/modules/ProjectDrawer.vue";
import IssueDevList from "@views/release/IssueDevList.vue";

const DetailListItem = DetailList.Item

export default {
  name: 'PublishDetail',
  components: {
    IssueDevList,
    ProjectDrawer,
    GenTextDrawer,
    PageLayout,
    DetailList,
    DetailListItem,
    JMarkdownEditor,
    PublishlistForm
  },
  mixins: [mixinDevice],
  data() {
    return {
      title: '',
      width: 900,
      visible: false,
      disableSubmit: false,
      txtTitle: '文本信息',
      txtContent: '',
      publishForm: {},
      publishStats: {},
      sprintStages: {},
      publishlistId: '',
      isMarkDown: true,
      issueList: [],
      testingCurrent: 0,
      customStyle: 'background: #fff;border-radius:4px;margin-bottom: 5px;border: 0;overflow: hidden',
      url: {
        queryById: '/release/queryById',
        genReleaseNote: '/release/generateReleaseNoteContent',
        genReleaseEmail: '/release/generateReleaseMailContent',
        genPackagePr: '/release/generateProductPackagePRContent',
        genHandbookChPr: '/release/generateProductHandbookPRChContent',
        genHandbookEnPr: '/release/generateProductHandbookPREnContent',
        genWebsite: '/release/generateWebsiteContent',
        issueList: "/release/issue/list",
      }
    }
  },
  created() {
    this.publishlistId = this.$route.query.id
    this.initDictConfig()
    this.reloadData()
  },
  mounted() {
  },
  computed: {
    sprintCurrent() {
      let ss = Object.keys(this.sprintStages).map((key) => this.sprintStages[key].text);
      return ss.indexOf(this.publishForm.scrumStage)
    },
    currentStage() {
      return
    }
  },
  methods: {
    initDictConfig() {
      //初始化字典 - sprint 迭代阶段
      initDictOptions('sprint_stage').then((res) => {
        if (res.success) {
          this.sprintStages = res.result;
          console.log('sprintStages', this.sprintStages)
        }
      });
    },
    reloadData() {
      this.loadDetial()
      this.loadIssues()
    },
    loadDetial() {
      this.queryParam = { id: this.publishlistId }
      getAction(this.url.queryById, this.queryParam).then((res) => {
        if (res.success) {
          this.publishForm = res.result
        } else {
          this.$message.error(res.message)
        }
      })
    },
    loadIssues(pn) {
      this.loading = true;
      var params = {}
      params.publishlistId = this.publishlistId
      params.pageNo = pn || 1
      params.pageSize = 10
      getAction(this.url.issueList, params).then((res) => {
        if (res.success) {
          this.issueList = res.result.records || res.result;
          // this.$refs.issueList.dataSource = this.issueList
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    handleReleaseNote() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genReleaseNote, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': 'ReleaseNote',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'ReleaseNote'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleReleaseMail() {
      this.isMarkDown = false
      const params = { id: this.publishlistId }
      getAction(this.url.genReleaseEmail, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': 'Release Email',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'ReleaseMail'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handlePackagePR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genPackagePr, params).then((res) => {
        if(res.success){
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': '打包 PR 生成',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'ProductPackagePRContent'
          })
        }else{
          this.$message.error(res.message)
        }
      })
    },
    handleHandbookChPR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genHandbookChPr, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': '手册中文 PR 生成',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'HandBookPRChContent'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleHandbookEnPR() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genHandbookEnPr, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': '手册英文 PR 生成',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'HandBookPREnContent'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleWebsite() {
      this.isMarkDown = true
      const params = { id: this.publishlistId }
      getAction(this.url.genWebsite, params).then((res) => {
        if (res.success) {
          console.log(res.result)
          this.txtContent = res.result
          this.$refs.modalForm.view({
            'publishlistId': this.publishlistId,
            'title': '官网内容生成',
            'content': res.result,
            'isMarkDown': this.isMarkDown,
            'templateType': 'Website'
          })
        } else {
          this.$message.error(res.message)
        }
      })
    },
    modalFormOk() { },
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.edit(record)
      })
    },
    submitCallback() {
      this.$emit('ok')
      this.visible = false
    },
    publish() {

    },
    handlePakcage() { },
    handleQuard() { },
    handleStep() { },
  }
}
</script>

<style lang="less" scoped>
.detail-layout {
  margin-left: 44px;
}

.text {
  color: rgba(0, 0, 0, .45);
}

.heading {
  color: rgba(0, 0, 0, .85);
  font-size: 20px;
}

.no-data {
  color: rgba(0, 0, 0, .25);
  text-align: center;
  line-height: 64px;
  font-size: 16px;

  i {
    font-size: 24px;
    margin-right: 16px;
    position: relative;
    top: 3px;
  }
}

.mobile {
  .detail-layout {
    margin-left: unset;
  }

  .text {}

  .status-list {
    text-align: left;
  }
}
</style>