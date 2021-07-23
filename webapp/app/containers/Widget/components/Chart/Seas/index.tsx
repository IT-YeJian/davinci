/*
 * <<
 * Davinci
 * ==
 * Copyright (C) 2016 - 2017 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

import * as React from 'react'
import { IChartProps } from './../'
import animate, {stop, delay} from 'animateplus'
import { Tooltip } from 'antd'
import { decodeMetricName } from 'containers/Widget/components/util'

const styles = require('./Chart.less')

function swing (elements: any, curnL: any, curnT: any, speed: any) {
    const clientWidth = document.getElementById('seasfish').clientWidth
    const clientHeight = document.getElementById('seasfish').clientHeight
    const random = (min, max) =>
    Math.random() * (max - min) + min

    const newCurnT = random(10, clientHeight)
    const newCurnL = random(10, clientWidth)
    if (newCurnL > curnL) {
        const imgSrc = elements.children[0].children[1].src
        if (imgSrc.indexOf('_2') > 1) {
            elements.children[0].children[1].src = imgSrc.substr(0, imgSrc.indexOf('_2')) + '_1.gif'
        }
    } else {
        const imgSrc = elements.children[0].children[1].src
        if (imgSrc.indexOf('_1') > 1) {
            elements.children[0].children[1].src = imgSrc.substr(0, imgSrc.indexOf('_1')) + '_2.gif'
        }
    }

    const durationRand = Math.ceil(Math.random() * 3000 + 3000 * speed)
    elements.addEventListener('click', ({target}) => stop(target))
    animate({
        elements,
        easing: 'linear',
        duration: durationRand,
        direction: 'normal',
        loop: false,
        transform: [`translateX(${curnL}px) translateY(${curnT}px)`, `translateX(${newCurnL}) translateY(${newCurnT})`]
    })
    .then(
        function () {
            swing(elements, newCurnL, newCurnT, speed)
        }
    )
  }

export class Seas extends React.PureComponent<IChartProps, {}> {

  private testOver (target, metricName, metricValue) {
    const conttentText = target.previousElementSibling.innerText
    if (conttentText.indexOf(' / ') > 1) {
        Object.assign(target.previousElementSibling.style, {
            position: 'absolute',
            cursor: 'default',
            height: '50px',
            width: '160px',
            backgroundImage: 'url("https://aybc.oss-cn-beijing.aliyuncs.com/sc/title.png")',
            backgroundSize: '100%',
            backgroundRepeat: 'no-repeat',
            color: 'rgb(38,141,177)',
            fontFamily: '微软雅黑',
            fontSize: '17px',
            top: '-25px',
            paddingTop: '0px',
            paddingLeft: '35px'
        })
        target.previousElementSibling.innerText = conttentText.substr(0, conttentText.indexOf(' / '))
    } else {
        Object.assign(target.previousElementSibling.style, {
            position: 'absolute',
            cursor: 'default',
            height: '90px',
            width: '260px',
            backgroundImage: 'url("https://aybc.oss-cn-beijing.aliyuncs.com/sc/titlep.gif")',
            backgroundSize: '100%',
            backgroundRepeat: 'no-repeat',
            color: 'rgb(38,141,177)',
            fontFamily: '微软雅黑',
            fontSize: '17px',
            top: '-65px',
            paddingTop: '35px',
            paddingLeft: '45px'
        })
        target.previousElementSibling.innerText = conttentText + ' / ' + metricName + '：' + metricValue
    }
  }

  private createFish (speed, cols, metrics, data) {
    const list = (length, callback) =>
    Array.from(new Array(length), (hole, index) => callback(index))

    const random = (min, max) =>
    Math.random() * (max - min) + min

    const randomColor = () =>
        `${palette[Math.floor(random(0, palette.length))]}`

    const palette = [
        'haixingnv_1'
    ]

    const elements = data.forEach((record) => {
        const colName = cols[0].name
        const metricName = decodeMetricName(metrics[0].name)
        const agg = metrics[0].agg

        const fishDiv = document.createElement('div')
        const toolDiv = document.createElement('Tooltip')
        toolDiv.title = colName + '：' + record[colName]
        const textDiv = document.createElement('div')
        Object.assign(textDiv.style, {
            position: 'absolute',
            cursor: 'default',
            height: '50px',
            width: '160px',
            backgroundImage: 'url("https://aybc.oss-cn-beijing.aliyuncs.com/sc/title.png")',
            backgroundSize: '100%',
            backgroundRepeat: 'no-repeat',
            color: 'rgb(38,141,177)',
            fontFamily: '微软雅黑',
            fontSize: '17px',
            top: '-25px',
            paddingLeft: '35px'
        })
        textDiv.innerText = colName + '：' + record[colName]

        const fishImg = document.createElement('img')
        Object.assign(fishDiv.style, {
            position: 'absolute',
            zIndex: '99999'
        })
        const size = random(100, 200)
        fishImg.src = 'https://aybc.oss-cn-beijing.aliyuncs.com/' + randomColor() + '.gif'
        Object.assign(fishImg.style, {
            width: `${size}px`,
            left: `${random(0, 100)}%`,
            top: `${random(0, 100)}%`,
            position: 'absolute'
        })
        toolDiv.append(textDiv)
        toolDiv.append(fishImg)
        fishDiv.append(toolDiv)
        document.getElementById('seasfish').append(fishDiv)
        
        fishDiv.addEventListener('click', ({target}) => {this.testOver(target, metricName, record[`${agg}(${metricName})`] || 0)})
        fishImg.addEventListener('onmouseover', function () { console.log('监听输出666') })

        const curnL = `${random(100, 1000)}`
        const curnT = `${random(100, 1000)}`
        // TODO createFish和swing 可合并一下
        swing(fishDiv, curnL, curnT, speed)
        return fishDiv
    })
  }

  public componentDidMount  () {
    const { cols, rows, metrics, data, chartStyles } = this.props
    const { seas } = chartStyles
    const { theme, speed } = seas
    const domSeasfish = document.getElementById('seasfish')
    // 增加背景图片
    domSeasfish.style.backgroundImage = 'url("https://aybc.oss-cn-beijing.aliyuncs.com/bg/' + theme + '.jpg")'
    domSeasfish.style.backgroundSize = '100% 100%'
    domSeasfish.style.backgroundRepeat = 'no-repeat'
    if (data.length > 0) {
        this.createFish(speed, cols, metrics, data)
    }
  }

  public render () {
    const { cols, rows, metrics, data, chartStyles } = this.props
    const { seas } = chartStyles
    const { theme, speed } = seas
    // 从其它图表切换过来不走此分支
    if (document.getElementById('seasfish') != null && data.length > 0) {
        const domSeasfish = document.getElementById('seasfish')
        while (domSeasfish.children.length > 0) {
            domSeasfish.removeChild(domSeasfish.children[domSeasfish.children.length - 1])
        }
        // 增加背景图片
        domSeasfish.style.backgroundImage = 'url("https://aybc.oss-cn-beijing.aliyuncs.com/bg/' + theme + '.jpg")'
        domSeasfish.style.backgroundSize = '100% 100%'
        domSeasfish.style.backgroundRepeat = 'no-repeat'
        this.createFish(speed, cols, metrics, data)
    }
    return (
      <div id="seasfish" className={styles.iframePage}>
          <Tooltip title="增加鱼">
          </Tooltip>
      </div>
    )
  }
}

export default Seas
