<template>
    <div class="charts-lint">
        <div class="breadcrumb">
            <div class="breadcrumb-name">
                <bk-tab :active.sync="active" @tab-change="handleTableChange" type="unborder-card">
                    <bk-tab-panel
                        v-for="(panel, index) in panels"
                        v-bind="panel"
                        :key="index">
                    </bk-tab-panel>
                </bk-tab>
            </div>
        </div>
        <div class="main-container">
            <div>
                <bk-form :label-width="60">
                    <container class="cc-container">
                        <div class="cc-col">
                            <bk-form-item :label="$t('工具')">
                                <bk-select v-model="toolId" @selected="handleSelectTool" :clearable="false" searchable>
                                    <bk-option-group
                                        v-for="group in toolList"
                                        :name="group.name"
                                        :key="group.key">
                                        <bk-option v-for="option in group.toolList"
                                            :key="option.toolName"
                                            :id="option.toolName"
                                            :name="option.toolDisplayName">
                                        </bk-option>
                                    </bk-option-group>
                                </bk-select>
                            </bk-form-item>
                        </div>
                        <div class="cc-col">
                            <bk-form-item :label="$t('日期')">
                                <bk-date-picker v-model="searchParams.daterange" :type="'daterange'" :options="pickerOptions"></bk-date-picker>
                            </bk-form-item>
                        </div>
                    </container>
                </bk-form>
            </div>
            <div class="trend-wrapper">
                <div class="trend-box">
                    <div class="trend-charts">
                        <div id="newTrendChart" ref="newTrendChart"></div>
                    </div>
                    <div class="trend-table">
                        <div class="table">
                            <bk-table :data="trendTableData" :outer-border="false">
                                <bk-table-column :label="$t('日期')" prop="tips" align="center"></bk-table-column>
                                <bk-table-column :label="$t('新问题数')" align="center">
                                    <template slot-scope="{ row, column, $index }">
                                        <a v-if="$index === trendTableData.length - 1" @click="handleHref({ defectType: 1 })" href="javascript:;">{{row.newCount}}</a>
                                        <span v-else>{{row.newCount}}</span>
                                    </template>
                                </bk-table-column>
                                <div slot="empty">
                                    <div class="codecc-table-empty-text">
                                        <img src="../../images/empty.png" class="empty-img">
                                        <div>{{$t('暂无数据')}}</div>
                                    </div>
                                </div>
                            </bk-table>
                        </div>
                    </div>
                </div>
                <div class="trend-box">
                    <div class="trend-charts">
                        <div id="hisTrendChart" ref="hisTrendChart"></div>
                    </div>
                    <div class="trend-table">
                        <div class="table">
                            <bk-table :data="trendTableData" :outer-border="false">
                                <bk-table-column :label="$t('日期')" prop="tips" align="center"></bk-table-column>
                                <bk-table-column :label="$t('历史问题数')" align="center">
                                    <template slot-scope="{ row, column, $index }">
                                        <a v-if="$index === trendTableData.length - 1" @click="handleHref({ defectType: 2 })" href="javascript:;">{{row.historyCount}}</a>
                                        <span v-else>{{row.historyCount}}</span>
                                    </template>
                                </bk-table-column>
                                <div slot="empty">
                                    <div class="codecc-table-empty-text">
                                        <img src="../../images/empty.png" class="empty-img">
                                        <div>{{$t('暂无数据')}}</div>
                                    </div>
                                </div>
                            </bk-table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="authors-wrapper">
                <div class="authors-charts">
                    <div id="newAuthorsChart" ref="newAuthorsChart"></div>
                </div>
                <div class="authors-table">
                    <div class="table">
                        <bk-table :data="newAuthorsTableData" :outer-border="false">
                            <bk-table-column :label="$t('问题处理人')" prop="authorName" align="center"></bk-table-column>
                            <bk-table-column :label="$t('总数')" prop="total" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, defectType: 1 })">{{row.total}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ defectType: 1 })">{{row.total}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('严重')" prop="serious" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 1, defectType: 1 })">{{row.serious}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 1, defectType: 1 })">{{row.serious}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('一般')" prop="normal" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 2, defectType: 1 })">{{row.normal}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 2, defectType: 1 })">{{row.normal}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('提示')" prop="prompt" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 4, defectType: 1 })">{{row.prompt}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 4, defectType: 1 })">{{row.prompt}}</a>
                                </template>
                            </bk-table-column>
                            <div slot="empty">
                                <div class="codecc-table-empty-text">
                                    <img src="../../images/empty.png" class="empty-img">
                                    <div>{{$t('暂无数据')}}</div>
                                </div>
                            </div>
                        </bk-table>
                    </div>
                </div>
            </div>
            <div class="authors-wrapper">
                <div class="authors-charts">
                    <div id="hisAuthorsChart" ref="hisAuthorsChart"></div>
                </div>
                <div class="authors-table">
                    <div class="table">
                        <bk-table :data="hisAuthorsTableData" :outer-border="false">
                            <bk-table-column :label="$t('问题处理人')" prop="authorName" align="center"></bk-table-column>
                            <bk-table-column :label="$t('总数')" prop="total" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, defectType: 2 })">{{row.total}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ defectType: 2 })">{{row.total}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('严重')" prop="serious" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 1, defectType: 2 })">{{row.serious}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 1, defectType: 2 })">{{row.serious}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('一般')" prop="normal" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 2, defectType: 2 })">{{row.normal}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 2, defectType: 2 })">{{row.normal}}</a>
                                </template>
                            </bk-table-column>
                            <bk-table-column :label="$t('提示')" prop="prompt" align="center">
                                <template slot-scope="{ row }">
                                    <a v-if="row.authorName !== 'Total'" href="javascript:;" @click="handleHref({ author: row.authorName, severity: 4, defectType: 2 })">{{row.prompt}}</a>
                                    <a v-else href="javascript:;" @click="handleHref({ severity: 4, defectType: 2 })">{{row.prompt}}</a>
                                </template>
                            </bk-table-column>
                            <div slot="empty">
                                <div class="codecc-table-empty-text">
                                    <img src="../../images/empty.png" class="empty-img">
                                    <div>{{$t('暂无数据')}}</div>
                                </div>
                            </div>
                        </bk-table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import chart from '@/mixins/chart'
    import { format } from 'date-fns'

    export default {
        components: {
        },
        mixins: [chart],
        data () {
            const query = this.$route.query

            return {
                panels: [
                    { name: 'defect', label: this.$t('问题管理') },
                    { name: 'report', label: this.$t('数据报表') }
                ],
                trendTableData: [],
                newAuthorsTableData: [],
                hisAuthorsTableData: [],
                searchParams: {
                    taskId: this.$route.params.taskId,
                    toolId: this.$route.params.toolId,
                    daterange: [query.startTime, query.endTime]
                },
                newTrendChart: undefined,
                hisTrendChart: undefined,
                newAuthorsChart: undefined,
                hisAuthorsChart: undefined
            }
        },
        computed: {
            pickerOptions () {
                return {
                    disabledDate (time) {
                        return time.getTime() > Date.now()
                    }
                }
            }
        },
        watch: {
            searchParams: {
                handler () {
                    this.fetchLintList().then(res => {
                        this.initTrend(res.chartLegacys && res.chartLegacys.legacyList)
                        this.initAuthor('new', res.chartAuthors && res.chartAuthors.newAuthorList)
                        this.initAuthor('his', res.chartAuthors && res.chartAuthors.historyAuthorList)
                    })
                },
                deep: true
            }
        },
        created () {
            // this.fetchLintList()
        },
        methods: {
            formatTime (date, token, options = {}) {
                return date ? format(Number(date), token, options) : ''
            },
            async fetchLintList () {
                this.searchParams.daterange[0] = this.searchParams.daterange[0] > Date.now() ? Date.now() : this.searchParams.daterange[0]
                this.searchParams.daterange[1] = this.searchParams.daterange[1] > Date.now() ? Date.now() : this.searchParams.daterange[1]
                this.searchParams.daterange[0] = this.searchParams.daterange[0] < this.searchParams.daterange[1] ? this.searchParams.daterange[0] : this.searchParams.daterange[1]
                const daterange = this.searchParams.daterange
                let startTime = this.formatTime(daterange[0], 'YYYY-MM-DD')
                startTime = startTime === 'Invalid Date' ? '' : startTime
                let endTime = this.formatTime(daterange[1], 'YYYY-MM-DD')
                endTime = endTime === 'Invalid Date' ? '' : endTime
                const params = { ...this.searchParams, startTime, endTime, showLoading: true }
                const res = await this.$store.dispatch('defect/report', params)
                return res
            },
            initTrend (elemList = []) {
                this.trendTableData = elemList.reverse()
                const xAxisData = elemList.map(item => item.tips)
                const newCount = elemList.map(item => item.newCount)
                const historyCount = elemList.map(item => item.historyCount)

                const optionNew = {
                    title: {
                        text: this.$t('新问题遗留趋势')
                    },
                    legend: {
                        data: [{
                            name: this.$t('新问题数')
                        }]
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    yAxis: {
                        splitNumber: 4,
                        minInterval: 1
                    },
                    grid: {
                        left: '50'
                    },
                    series: [{
                        name: this.$t('新问题数'),
                        data: newCount
                    }]
                }
                this.handleChartOption('newTrendChart', optionNew, 'chartLineOption')

                const optionHis = {
                    title: {
                        text: this.$t('历史问题遗留趋势')
                    },
                    legend: {
                        data: [{
                            name: this.$t('历史问题数')
                        }]
                    },
                    xAxis: {
                        data: xAxisData
                    },
                    yAxis: {
                        splitNumber: 4,
                        minInterval: 1
                    },
                    grid: {
                        left: '50'
                    },
                    series: [{
                        name: this.$t('历史问题数'),
                        data: historyCount,
                        itemStyle: {
                            normal: {
                                color: '#2dcb56'
                            }
                        },
                        lineStyle: {
                            normal: {
                                color: '#2dcb56'
                            }
                        }
                    }]
                }
                this.handleChartOption('hisTrendChart', optionHis, 'chartLineOption')
            },
            initAuthor (authorType, list = {}) {
                const authorTypeMap = {
                    new: {
                        data: 'newAuthorsTableData',
                        title: this.$t('新问题处理人分布'),
                        chart: 'newAuthorsChart'
                    },
                    his: {
                        data: 'hisAuthorsTableData',
                        title: this.$t('历史问题处理人分布'),
                        chart: 'hisAuthorsChart'
                    }
                }
                this.handleInitAuthor(authorTypeMap, authorType, list)
            },
            handleHref (query) {
                this.resolveHref('defect-lint-list', query)
            }
        }
    }
</script>

<style lang="postcss" scoped>
    @import '../../css/variable.css';
    @import '../../css/charts.css';

    .charts-lint {
        padding: 16px 20px 0px 16px;
        .breadcrumb {
            padding: 0px!important;
            .breadcrumb-name {
                background: white;
            }
        }
        .main-container {
            /* padding: 20px 33px 0!important;
            margin: 0 -13px!important; */
            border-top: 1px solid #dcdee5;
            margin: 0px!important;
            background: white;
        }
        .layout-inner .main-content {
            background: #f5f7fa;
        }
        >>> .bk-table {
            th, td {
                height: 28px;
                
                &>.cell {
                    height: 28px;
                    line-height: 28px;
                }
            }
        }
        .authors-wrapper {
            >>> .bk-table-row-last {
                font-weight: bold;
            }
        }
        .trend-wrapper {
            display: flex;

            .trend-box {
                width: calc(50% - 7.5px);
                background: #fff;
                border: 1px solid $borderColor;
                margin-bottom: 15px;

                .trend-charts, .trend-table {
                    width: 100%;
                    padding: 15px;
                }

                &:first-of-type {
                    margin-right: 15px;
                }
                #newTrendChart, #hisTrendChart {
                    width: 100%;
                    height: 200px;
                }
            }
        }
        
        .authors-wrapper {
            width: 100%;
            background: #fff;
            margin-bottom: 15px;
            border: 1px solid $borderColor;

            .authors-charts, .authors-table {
                padding: 20px 15px;
            }
            #newAuthorsChart, #hisAuthorsChart {
                width: 100%;
                height: 215px;
            }
        }
    }
</style>
