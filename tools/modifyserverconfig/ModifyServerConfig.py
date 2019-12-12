# -*- coding: utf-8 -*
# python 3.7
# 修改serverconfig

import lxml.etree as ET

# 读取并解析xml文件
def read_xml(in_path):
    tree = ET.parse(in_path)
    return tree

# 查找某个路径匹配的所有节点
def find_nodes(tree, path):
    return tree.findall(path)

# 修改对应进程xml
def changeXml(id):
    A = '1'
    # fight.xml
    tree = read_xml("../../Config/serverconfig/fight.xml")
    element = tree.find("server")
    element.set("serverId", A + "07" + id)
    tree.write("../../Config/serverconfig/fight.xml", xml_declaration=True, encoding='utf-8')
    # game.xml
    tree = read_xml("../../Config/serverconfig/game.xml")
    element = tree.find("server")
    element.set("serverId", A + "02" + id)
    tree.write("../../Config/serverconfig/game.xml", xml_declaration=True, encoding='utf-8')
    # login.xml
    tree = read_xml("../../Config/serverconfig/login.xml")
    element = tree.find("server")
    element.set("serverId", A + "01" + id)
    tree.write("../../Config/serverconfig/login.xml", xml_declaration=True, encoding='utf-8')
    # match.xml
    tree = read_xml("../../Config/serverconfig/match.xml")
    element = tree.find("server")
    element.set("serverId", A + "06" + id)
    tree.write("../../Config/serverconfig/match.xml", xml_declaration=True, encoding='utf-8')
    # proxy.xml
    tree = read_xml("../../Config/serverconfig/proxy.xml")
    element = tree.find("server")
    element.set("serverId", A + "05" + id)
    tree.write("../../Config/serverconfig/proxy.xml", xml_declaration=True, encoding='utf-8')

# 修改region.xml
def changeRegionXml(regionId, regionName):
    tree = read_xml("../../Config/serverconfig/region.xml")
    element = tree.find("regionList/region")
    element.set("regionId", regionId)
    element.set("gameName", regionName)
    tree.write("../../Config/serverconfig/region.xml", xml_declaration=True, encoding='utf-8')

# 修改server.xml
def changeServerXml(ip, id):
    A = '1'
    tree = read_xml("../../Config/serverconfig/server.xml")

    gameElement = tree.find("gameHost/host")
    gameElement.set("serverId", A + "02" + id)
    gameElement.set("addr", ip)

    zoneElement = tree.find("zoneHost/host")
    zoneElement.set("serverId", A + "03" + id)

    talkElement = tree.find("talkHost/host")
    talkElement.set("serverId", A + "04" + id)

    proxyServer = tree.find("proxyServer/server")
    proxyServer.set("serverId", A + "05" + id)

    tree.write("../../Config/serverconfig/server.xml", xml_declaration=True, encoding='utf-8')

# execute
print('######欢迎入坑！######')
print('请输入本机IP：')
ip = input()
print('请输入服务器编号（2位数例如06，17）：')
id = input()
print('请输入小区号：')
regionId = input()
print('请输入小区名：')
regionName = input()

changeRegionXml(regionId, regionName)
changeServerXml(ip, id)
changeXml(id)

input("Press <enter>")