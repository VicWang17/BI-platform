import React, {useEffect, useState} from 'react';
import {
  Avatar,
  Button, Card,
  Checkbox,
  Col,
  ColorPicker, Divider,
  Form,
  List, message,
  Radio,
  Rate,
  Row,
  Select,
  Slider,
  Space,
  Switch,
  Upload,
} from 'antd';
import {listMyChartByPageUsingPost} from "@/services/ant-design-pro/chartController";
import ReactECharts from "echarts-for-react";
import {getInitialState} from "@/app";
import {useModel} from "@@/exports";
import Search from "antd/es/input/Search";




const History: React.FC = () => {
  const initSearchParams = {
    current: 1,
    pageSize: 6,
  }

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({...initSearchParams});
  const [chartList, setChartList] = useState<API.Chart[]>();
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState ?? {};
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listMyChartByPageUsingPost(searchParams);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
      } else {
        message.error('Get Chart failed')
      }
    } catch (e: any) {
      message.error('Get Chart failed, ' + e.message);
    }
    setLoading(false);
  }

  useEffect(() => {
    loadData();
  }, [searchParams]);


  return (
    <div>
      <div>
        <Search placeholder = "input search loading with enterButton" enterButton onSearch={(value)=>{
          setSearchParams({
            ...initSearchParams,
            name: value,
          })
        }}/>
      </div>
      <div style = {{marginBottom: 55}} />
      <List
        itemLayout="horizontal"
        grid={{gutter:16, xs:1, sm:1, md:2, lg:2, xl:3, xxl:3}}
        size="large"
        pagination={{
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            })
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
        }}
        loading={loading}
        dataSource={chartList}
        renderItem={(item) => (
          <List.Item
            key={item.id}
            >
            <List.Item.Meta
              avatar={<Avatar src={currentUser && currentUser.userAvatar} />}
              title={item.name}
              description={item.chartType ? 'Chart Type：' + item.chartType : undefined}
            />
            {'Goal: ' + item.goal}
            <ReactECharts option={item.getChart && JSON.parse(item.getChart)}/>
          </List.Item>
        )}
      />
    </div>

  );
}

export default History;
