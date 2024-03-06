import { InboxOutlined, UploadOutlined } from '@ant-design/icons';
import React, {useState} from 'react';
import {
  Button,
  Checkbox,
  Col,
  ColorPicker, Divider,
  Form,
  InputNumber, message,
  Radio,
  Rate,
  Row,
  Select,
  Slider,
  Space,
  Switch,
  Upload,
} from 'antd';
import TextArea from "antd/es/input/TextArea";
import {genChartByAiUsingPost} from "@/services/ant-design-pro/chartController";
import ReactECharts from 'echarts-for-react';
import Card from "antd/es/card/Card";


const { Option } = Select;

const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 14 },
};

const normFile = (e: any) => {
  console.log('Upload event:', e);
  if (Array.isArray(e)) {
    return e;
  }
  return e?.fileList;
};



const App: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>(undefined);
  const [submitting, setSubmitting] = useState<boolean>(false);



  const onFinish = async (values: any) => {
    if(submitting){
      return;
    }
    setSubmitting(true);
    setChart(undefined);
    setOption(undefined);

    const params ={
      ...values,
      file: undefined
    }
    try{
      const res = await genChartByAiUsingPost(params, {}, values.file.file.originFileObj)
      console.log(res);
      if (!res?.data){
        message.error("Unsuccessful.")
      }else {
        message.success("Analysis successful!");
        const chartOption = JSON.parse(res.data.genChart ?? '');
        if (!chartOption){
          throw new Error('Chart Code Error');
        }else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    }catch (e: any){
      message.error("Unsuccessful." + e.message);
    }

    setSubmitting(false);

  };
  return (
    <div  className="add-chart">
      <Row gutter={24}>
        <Col span = {12}>
          <Card title="Intelligence Analyze">
            <Form
              name="add-chart"
              labelAlign="left"
              labelCol={{ span:5}}
              wrapperCol={{ span: 16 }}
              onFinish={onFinish}
              initialValues={{
                //初始化数据
              }}
              style={{ maxWidth: 600 }}
            >
              <Form.Item
                name="chartType"
                label="Chart Type"
              >
                <Select placeholder="Please select a chart type">
                  <Option value="bar">Bar chart</Option>
                  <Option value="Basic Line Chart">Basic Line Chart</Option>
                  <Option value="Smoothed Line Chart">Smoothed Line Chart</Option>
                  <Option value="Basic Scatter Chart">Scatter Chart</Option>
                  <Option value="Basic Radar Chart">Basic Radar Chart</Option>
                </Select>
              </Form.Item>

              <Form.Item name="name" label="Chart's Name">
                <TextArea placeholder="please input the chart's name" autoSize/>
              </Form.Item>

              <Form.Item name="goal" label="Goal" rules={[{ required: true, message: 'Please input!' }]}>
                <TextArea placeholder="Please input your analysis requirement. e.g. Analyze user growth in the file."/>
              </Form.Item>

              <Form.Item
                name="file"
                label="Data File"
                extra="Support .xlsx"
              >
                <Upload name="logo" maxCount={1}>
                  <Button icon={<UploadOutlined />}>Click to upload</Button>
                </Upload>
              </Form.Item>
              {/*<Form.Item
      name="color-picker"
      label="ColorPicker"//13254
      rules={[{ required: true, message: 'color is required!' }]}
    >
      <ColorPicker />
    </Form.Item>*/}

              <Form.Item wrapperCol={{ span: 16, offset: 5 }}>
                <Space>
                  <Button type="primary" htmlType="submit" loading={submitting} disabled={submitting}>
                    Submit
                  </Button>
                  <Button htmlType="reset">reset</Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>

        </Col>
        <Col span={12}>
          <Card title="Result">
            {
              chart?.genResult ?? <div>Please submit in the left first</div>
          }
          </Card>
          <Divider/>
          <Card title="Chart">
            {
            option ? <ReactECharts option={option} /> : <div>Please submit in the left first</div>
          }
          </Card>
        </Col>

      </Row>




    </div>
  );
}

export default App;
