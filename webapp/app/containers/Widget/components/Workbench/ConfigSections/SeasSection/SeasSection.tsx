import React from 'react'
import debounce from 'lodash/debounce'
import { Row, Col, Select, Input } from 'antd'
import { charSeasThemeOptions, chartSeasSpeedTypeOptions } from '../constants'

const styles = require('../../Workbench.less')

export interface ISeasConfig {
  content: string
  theme: string
  speed: string
}

interface ISeasSectionProps {
  title: string
  config: ISeasConfig
  onChange: (prop: string, value: any) => void
}

export class SeasSection extends React.PureComponent<ISeasSectionProps, {}> {

  private debounceInputChange = null

  constructor (props: ISeasSectionProps) {
    super(props)
    this.debounceInputChange = debounce(props.onChange, 1500)
  }

  private inputChange = (prop) => (e: React.ChangeEvent<HTMLInputElement>) => {
    this.debounceInputChange(prop, e.target.value)
  }

  private selectChange = (prop) => (value) => {
    this.props.onChange(prop, value)
  }

  public render () {
    const { title, config, onChange } = this.props
    const { content, theme, speed } = config

    return (
      <div className={styles.paneBlock}>
        <h4>{title}</h4>
        <br/>
        <div className={styles.blockBody}>
          <Row gutter={8} type="flex" align="middle" className={styles.blockRow}>
            <Col span={4}>标题</Col>
            <Col span={20}>
              <Input onChange={this.inputChange('content')} placeholder="请输入标题" defaultValue={content}/>
            </Col>
          </Row>
          <br/>
          <Row gutter={8} type="flex" align="middle" className={styles.blockRow}>
            <Col span={4}>背景</Col>
            <Col span={20}>
                <Select
                    placeholder="背景"
                    className={styles.blockElm}
                    value={theme}
                    onChange={this.selectChange('theme')}
                >
                    {charSeasThemeOptions}
                </Select>
            </Col>
          </Row>
          <br/>
          <Row gutter={8} type="flex" align="middle" className={styles.blockRow}>
            <Col span={4}>速度</Col>
            <Col span={20}>
                <Select
                    placeholder="速度"
                    className={styles.blockElm}
                    value={speed}
                    onChange={this.selectChange('speed')}
                >
                    {chartSeasSpeedTypeOptions}
                </Select>
            </Col>
          </Row>
        </div>
      </div>
    )
  }
}

export default SeasSection
