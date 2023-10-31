import { ReactNode, useState } from "react";
import { Card, Tag, Input, Tabs, Row, Col, List, Space, Avatar } from "antd";
import MyIcon from "@/components/icon";
import "./index.less";
import { useStyle } from "./style";

const tagInitVal = [
  { value: "足球", color: "magenta" },
  { value: "跑步", color: "volcano" },
  { value: "web前端", color: "orange" },
  { value: "90后", color: "gold" },
];
function getRandomColor() {
  return "#" + Math.random().toString(16).slice(2, 8);
}

let listData = Array.from({ length: 10 }, (v, k) => ({
  href: "https://ant.design",
  title: `ant design part ${k + 1}`,
  avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
  description:
    "Ant Design, a design language for background applications, is refined by Ant UED Team.",
  content:
    "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.",
}));

listData = []

const IconText = ({ icon, text }: { icon: ReactNode, text: string }) => (
  <Space>
    {icon}
    {text}
  </Space>
);

const tabpanes = Array.from({ length: 3 }, (v, k) => ({
  key: k + '',
  label: `tab${k + 1}`,
  children: (<List
    itemLayout="vertical"
    size="large"
    header={<h2>Tab {k + 1}</h2>}
    dataSource={listData}
    renderItem={(item) => (
      <List.Item
        key={item.title}
        actions={[
          <IconText
            icon={<MyIcon type="icon_collection" />}
            text="156"
            key="list-vertical-star-o"
          />,
          <IconText
            icon={<MyIcon type="icon_zan" />}
            text="156"
            key="list-vertical-like-o"
          />,
          <IconText
            icon={<MyIcon type="icon_voice" />}
            text="2"
            key="list-vertical-message"
          />,
        ]}
        extra={
          <img
            width={272}
            alt="logo"
            src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
          />
        }
      >
        <List.Item.Meta
          avatar={<Avatar src={item.avatar} />}
          title={<a href={item.href}>{item.title}</a>}
          description={item.description}
        />
        {item.content}
      </List.Item>
    )}
  />)
})
);

export default function Person() {
  const [tags, setTag] = useState(tagInitVal);
  const [isInput, setInput] = useState(false);
  const [value, setVal] = useState("");
  const { styles } = useStyle()
  const addTags = () => {
    if (!value) {
      return setInput(false);
    }
    let tempTag = { value: value, color: getRandomColor() };
    setVal("");
    setTag([...tags, tempTag]);
    setInput(false);
  };
  return (
    <div className="person-container">
      <Row>
        <Col span={6}>
          <Card
          >
            <div className={styles.info}>
              <p>
                <MyIcon type="icon_edit" className="icon" />
                <a
                  href="https://www.cnblogs.com/kongyijilafumi/"
                  target="_blank"
                  rel="noreferrer"
                >
                  博客地址
                </a>
              </p>
            </div>
            <div className="tags">
              <div className="title">标签</div>
              <div className="wrapper">
                {tags.map((item) => (
                  <Tag color={item.color} key={item.color}>
                    {item.value}
                  </Tag>
                ))}
                {isInput ? (
                  <Input
                    placeholder="请输入"
                    className="ipt"
                    onBlur={addTags}
                    value={value}
                    onChange={(e) => setVal(e.target.value)}
                  />
                ) : (
                  <MyIcon type="icon_increase" onClick={() => setInput(true)} />
                )}
              </div>
            </div>
          </Card>
        </Col>
        <Col span={17} offset={1} className={styles.tabs}>
          <Tabs defaultActiveKey="1" items={tabpanes}> </Tabs>
        </Col>
      </Row>
    </div>
  );
}
Person.route = { [MENU_PATH]: "/details/person" };
