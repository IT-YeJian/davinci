import React from 'react'
import { IChartProps } from './index'
import chartlibs from '../../config/chart'
import echarts from 'echarts/lib/echarts'
import { ECharts } from 'echarts'
import 'echarts/extension/bmap/bmap'
import chartOptionGenerator from '../../render/chart'
<<<<<<< HEAD
=======
import { getTriggeringRecord } from '../util'
import geoData from 'assets/js/geo.js'
import 'echarts-gl/dist/echarts-gl'
>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
const styles = require('./Chart.less')

interface IChartStates {
  seriesItems: string[]
}
export class Chart extends React.PureComponent<IChartProps, IChartStates> {
  private asyncEmitTimer: NodeJS.Timer | null = null
  private container: HTMLDivElement = null
  private instance: ECharts
<<<<<<< HEAD
  constructor(props) {
=======
  private timerProvinceClick = null
  constructor (props) {
>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
    super(props)
    this.state = {
      seriesItems: []
    }
  }
  public componentDidMount() {
    this.renderChart(this.props)
  }

  public componentDidUpdate() {
    this.renderChart(this.props)
  }

  private renderChart = (props: IChartProps) => {
    const {
      selectedChart,
      renderType,
      getDataDrillDetail,
      isDrilling,
      onError
    } = props

    if (renderType === 'loading') {
      return
    }
    if (!this.instance) {
      this.instance = echarts.init(this.container, 'default')
      echarts.registerMap('test', require('../../../../assets/json/map/0.json'))
    } else {
      if (renderType === 'rerender') {
        this.instance.dispose()
        this.instance = echarts.init(this.container, 'default')
      }
      if (renderType === 'clear') {
        // this.instance.clear()
        // 解决百度地图切换问题
        this.instance.dispose()
        this.instance = echarts.init(this.container, 'default')
      }
    }

    try {
      this.instance.off('click')
      this.instance.on('click', (params) => {
        this.collectSelectedItems(params)
      })

      this.instance.setOption(
        chartOptionGenerator(
          chartlibs.find((cl) => cl.id === selectedChart).name,
          props,
          {
            instance: this.instance,
            isDrilling,
            getDataDrillDetail,
            selectedItems: this.props.selectedItems,
            callback: (seriesData) => {
              this.instance.off('click')
              this.instance.on('click', (params) => {
                this.collectSelectedItems(params, seriesData)
              })
            }
          }
        )
      )
      this.instance.resize()
    } catch (error) {
      if (onError) {
        onError(error)
      }
    }
  }

<<<<<<< HEAD
  public componentWillUnmount() {
    if (this.instance) {
      this.instance.off('click')
    }
    if (this.asyncEmitTimer) {
      clearTimeout(this.asyncEmitTimer)
    }
  }

  private collectSelectedItems = (params, seriesData?) => {
    const {
      data,
      selectedChart,
      onDoInteract,
      getDataDrillDetail,
      onSelectChartsItems,
      onCheckTableInteract
    } = this.props

    const { seriesItems } = this.state

=======
    if (selectedChart === 7) {
        this.container.oncontextmenu = () => {
            return false
        } // 屏蔽右键默认事件
        // this.instance.on('contextmenu', (params) => {
        //     this.mapReturn(params)
        // })
    }
    // this.instance.off('click')
    // this.instance.on('click', (params) => {
    //   if (selectedChart === 7) {
    //     this.mapClick(params)
    //   }
    //   this.collectSelectedItems(params)
    // })
    this.instance.resize()
  }

//   public mapClick = (params) => {
//     const { selectedChart, getDataDrillDetail, isDrilling } = this.props
//     const area = geoData.find((d) => d.name.includes(params.name))
//     if (area) {
//         echarts.registerMap('test', require('../../../../assets/json/map/' + area.id + '.json'))
//         // this.instance.clear()
//         this.instance.setOption(
//             chartOptionGenerator(
//             chartlibs.find((cl) => cl.id === selectedChart).name,
//             this.props,
//             {
//                 instance: this.instance,
//                 isDrilling,
//                 getDataDrillDetail,
//                 selectedItems: this.props.selectedItems
//             }
//             )
//         )
//     }
//   }
//   public mapReturn = (params) => {
//     const { selectedChart, getDataDrillDetail, isDrilling } = this.props
//     const area = geoData.find((d) => d.name.includes(params.name))
//     const parent = geoData.find((g) => g.id === area.parent)
//     // console.log(area)
//     if (area) {
//         echarts.registerMap('test', require('../../../../assets/json/map/' + parent.parent + '.json'))
//         this.instance.clear()
//         this.instance.setOption(
//             chartOptionGenerator(
//             chartlibs.find((cl) => cl.id === selectedChart).name,
//             this.props,
//             {
//                 instance: this.instance,
//                 isDrilling,
//                 getDataDrillDetail,
//                 selectedItems: this.props.selectedItems
//             }
//             )
//         )
//     }
//   }
  public collectSelectedItems = (params) => {
    const { data, onSelectChartsItems, selectedChart, onDoInteract, onCheckTableInteract } = this.props
>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
    let selectedItems = []
    let series = []
    if (this.props.selectedItems && this.props.selectedItems.length) {
      selectedItems = [...this.props.selectedItems]
    }
    let dataIndex = params.dataIndex
    if (selectedChart === 4) {
      dataIndex = params.seriesIndex
    }
    if (selectedItems.length === 0) {
      selectedItems.push(dataIndex)
    } else {
      const isb = selectedItems.some((item) => item === dataIndex)
      if (isb) {
        for (let index = 0, l = selectedItems.length; index < l; index++) {
          if (selectedItems[index] === dataIndex) {
            selectedItems.splice(index, 1)
            break
          }
        }
      } else {
        selectedItems.push(dataIndex)
      }
    }

    if (seriesData) {
      const { seriesIndex, dataIndex } = params
      const char = `${seriesIndex}&${dataIndex}`
      if (seriesItems && Array.isArray(seriesItems)) {
        series = seriesItems.includes(char)
          ? seriesItems.filter((item) => item !== char)
          : seriesItems.concat(char)
        this.setState({ seriesItems: series })
      }
    }
    const resultData = selectedItems.map((item, index) => {
      if (seriesData) {
        const seriesIndex = series[index] ? series[index].split('&')[0] : null
        return seriesData[seriesIndex] ? seriesData[seriesIndex][item] : []
      }
      return data[item]
    })
    const brushed = [{ 0: Object.values(resultData) }]
    const sourceData = Object.values(resultData)
    const isInteractiveChart = onCheckTableInteract && onCheckTableInteract()
    if (isInteractiveChart && onDoInteract) {
      const triggerData = sourceData
      onDoInteract(triggerData)
    }
    this.asyncEmitTimer = setTimeout(() => {
      if (getDataDrillDetail) {
        getDataDrillDetail(JSON.stringify({ range: null, brushed, sourceData }))
      }
    }, 500)
    if (onSelectChartsItems) {
      onSelectChartsItems(selectedItems)
    }
  }

  public render() {
    return (
      <div
        className={styles.chartContainer}
        ref={(f) => (this.container = f)}
      />
    )
  }
}

export default Chart
