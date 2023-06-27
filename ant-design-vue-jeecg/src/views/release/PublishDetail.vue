<template>
  <page-layout
    :title="'发布单：' + publishForm.name"
    logo="https://gw.alipayobjects.com/zos/rmsportal/nxkuOJlFJuAUhzlMTCEe.png"
  >
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
    <!-- <publish-info :publishForm="publishForm"></publish-info> -->
    <template slot="action">
      <a-button type="dashed" icon="reload" @click="reloadData"></a-button>
      <!-- <a-button type="primary" style="margin:0 4px" icon="cloud-upload" @click="publish">发布</a-button> -->
    </template>

    <!-- <a-card :bordered="false" title="Sprint 迭代进度">
      <a-steps :direction="(isMobile() && 'vertical') || 'horizontal'" :current="sprintCurrent" progressDot>
        <a-step v-for="item in sprintStages" :key="item.key" :title="item.text"></a-step>
      </a-steps>
    </a-card> -->

    <a-collapse :default-active-key="currentStage" :bordered="true">
      <template #expandIcon="props">
        <a-icon type="caret-right" :rotate="props.isActive ? 90 : 0" />
        <a-icon type="caret-right" :rotate="props.isActive ? 45 : 90" />
        <a href="#">hello</a>
      </template>
      <a-collapse-panel key="1" header="研发阶段" :style="customStyle">
        <a-row>
          <a-col :span="12">
            <issue-dev-list :pid="publishlistId" ref="issueList"></issue-dev-list>
          </a-col>
          <a-col :span="12">
            <pub-status-package-url-list stage="DEV" :publishForm="publishForm"></pub-status-package-url-list>
          </a-col>
        </a-row>
      </a-collapse-panel>
      <a-collapse-panel key="2" header="测试阶段" :style="customStyle">
        <pub-status-package-url-list stage="QA" :publishForm="publishForm"></pub-status-package-url-list>

        <!--        <a-steps direction="horizontal" :current="testingCurrent" progressDot>-->
        <!--          <a-step key="1" title="打包"></a-step>-->
        <!--          <a-step key="2" title="Quard"></a-step>-->
        <!--          <a-step key="3" title="Step"></a-step>-->
        <!--        </a-steps>-->
      </a-collapse-panel>
      <a-collapse-panel key="3" header="RC阶段" :style="customStyle">
        <pub-status-package-url-list stage="RC" :publishForm="publishForm"></pub-status-package-url-list>

      </a-collapse-panel>
      <a-collapse-panel key="4" header="交付阶段" :style="customStyle">
        <!-- actions -->
        <project-gen-actions :publishlistId="publishlistId"></project-gen-actions>
      </a-collapse-panel>
    </a-collapse>
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
import GenTextDrawer from '@views/release/modules/GenTextDrawer.vue'
import ProjectDrawer from '@views/release/modules/ProjectDrawer.vue'
import IssueDevList from '@views/release/IssueDevList.vue'

import PubStatusPackageUrlList from '@views/dev/PubStatusPackageUrlList.vue'

import ProjectGenActions from '@views/release/modules/ProjectGenActions.vue'
import PublishInfo from '@views/release/modules/PublishInfo.vue'

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
    PublishlistForm,
    PublishInfo,
    ProjectGenActions,
    PubStatusPackageUrlList
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
      // isMarkDown: true,
      issueList: [],
      testingCurrent: 0,
      customStyle: 'background: #fff;border-radius:4px;margin-top:0;margin-bottom: 5px;border: 0;overflow: hidden',
      url: {
        queryById: '/release/queryById',
        issueList: '/release/issue/list'
      }
    }
  },
  created() {
    this.publishlistId = this.$route.query.id
    this.initDictConfig()
    this.reloadData()
  },
  mounted() {},
  computed: {
    sprintCurrent() {
      let ss = Object.keys(this.sprintStages).map(key => this.sprintStages[key].text)
      return ss.indexOf(this.publishForm.scrumStage)
    },
    currentStage() {
      return '2'
    }
  },
  methods: {
    initDictConfig() {
      //初始化字典 - sprint 迭代阶段
      initDictOptions('sprint_stage').then(res => {
        if (res.success) {
          this.sprintStages = res.result
          console.log('sprintStages', this.sprintStages)
        }
      })
    },
    reloadData() {
      this.loadDetial()
      this.loadIssues()
    },
    loadDetial() {
      this.queryParam = { id: this.publishlistId }
      getAction(this.url.queryById, this.queryParam).then(res => {
        if (res.success) {
          this.publishForm = res.result
        } else {
          this.$message.error(res.message)
        }
      })
    },
    loadIssues(pn) {
      this.loading = true
      var params = {}
      params.publishlistId = this.publishlistId
      params.pageNo = pn || 1
      params.pageSize = 10
      getAction(this.url.issueList, params)
        .then(res => {
          if (res.success) {
            this.issueList = res.result.records || res.result
            // this.$refs.issueList.dataSource = this.issueList
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    modalFormOk() {},
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
    publish() {},
  }
}
</script>

<style lang="less" scoped></style>
