#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
车型数据更新脚本
用于从游戏数据中提取车型信息并更新静态数据文件
"""

import json
import requests
import argparse
from datetime import datetime
from typing import List, Dict, Any

class CarDataUpdater:
    def __init__(self, cars_file: str = "src/main/resources/static/cars.json"):
        self.cars_file = cars_file
        self.base_url = "http://localhost:8080/api"
    
    def load_current_data(self) -> Dict[str, Any]:
        """加载当前车型数据"""
        try:
            with open(self.cars_file, 'r', encoding='utf-8') as f:
                return json.load(f)
        except FileNotFoundError:
            return {
                "version": "1.0.0",
                "lastUpdated": datetime.now().strftime("%Y-%m-%d"),
                "gameVersion": "Forza Horizon 5",
                "cars": []
            }
    
    def save_data(self, data: Dict[str, Any]):
        """保存车型数据"""
        with open(self.cars_file, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        print(f"数据已保存到 {self.cars_file}")
    
    def update_version(self, data: Dict[str, Any], new_version: str = None):
        """更新版本信息"""
        if new_version:
            data["version"] = new_version
        else:
            # 自动递增版本号
            current_version = data.get("version", "1.0.0")
            major, minor, patch = map(int, current_version.split("."))
            data["version"] = f"{major}.{minor}.{patch + 1}"
        
        data["lastUpdated"] = datetime.now().strftime("%Y-%m-%d")
        return data
    
    def add_car(self, car_data: Dict[str, Any]) -> Dict[str, Any]:
        """添加新车型"""
        data = self.load_current_data()
        
        # 生成唯一ID
        car_id = f"car{len(data['cars']) + 1}"
        car_data["id"] = car_id
        
        # 检查是否已存在
        existing_cars = [car for car in data["cars"] if 
                        car["name"] == car_data["name"] and 
                        car["manufacturer"] == car_data["manufacturer"] and
                        car["year"] == car_data["year"]]
        
        if existing_cars:
            print(f"车型已存在: {car_data['name']} ({car_data['manufacturer']} {car_data['year']})")
            return data
        
        data["cars"].append(car_data)
        data = self.update_version(data)
        return data
    
    def remove_car(self, car_name: str, manufacturer: str, year: int) -> Dict[str, Any]:
        """删除车型"""
        data = self.load_current_data()
        
        original_count = len(data["cars"])
        data["cars"] = [car for car in data["cars"] if not (
            car["name"] == car_name and 
            car["manufacturer"] == manufacturer and 
            car["year"] == year
        )]
        
        removed_count = original_count - len(data["cars"])
        if removed_count > 0:
            data = self.update_version(data)
            print(f"已删除 {removed_count} 个车型")
        else:
            print("未找到匹配的车型")
        
        return data
    
    def update_car(self, car_name: str, manufacturer: str, year: int, updates: Dict[str, Any]) -> Dict[str, Any]:
        """更新车型信息"""
        data = self.load_current_data()
        
        for car in data["cars"]:
            if (car["name"] == car_name and 
                car["manufacturer"] == manufacturer and 
                car["year"] == year):
                
                car.update(updates)
                data = self.update_version(data)
                print(f"已更新车型: {car_name}")
                return data
        
        print("未找到匹配的车型")
        return data
    
    def reload_server_data(self):
        """重新加载服务器数据"""
        try:
            response = requests.post(f"{self.base_url}/admin/cars/reload")
            if response.status_code == 200:
                print("服务器数据重新加载成功")
            else:
                print(f"服务器数据重新加载失败: {response.status_code}")
        except requests.exceptions.RequestException as e:
            print(f"无法连接到服务器: {e}")
    
    def get_server_stats(self):
        """获取服务器统计信息"""
        try:
            response = requests.get(f"{self.base_url}/admin/cars/stats")
            if response.status_code == 200:
                stats = response.json()
                print("服务器统计信息:")
                print(f"  总车型数: {stats.get('totalCars', 0)}")
                print(f"  制造商数: {stats.get('manufacturers', 0)}")
                print(f"  数据版本: {stats.get('dataInfo', {}).get('version', 'unknown')}")
            else:
                print(f"获取统计信息失败: {response.status_code}")
        except requests.exceptions.RequestException as e:
            print(f"无法连接到服务器: {e}")

def main():
    parser = argparse.ArgumentParser(description="车型数据更新工具")
    parser.add_argument("--action", choices=["add", "remove", "update", "reload", "stats"], 
                       required=True, help="操作类型")
    parser.add_argument("--name", help="车型名称")
    parser.add_argument("--manufacturer", help="制造商")
    parser.add_argument("--year", type=int, help="年份")
    parser.add_argument("--category", help="车型分类")
    parser.add_argument("--pi", type=int, help="PI等级")
    parser.add_argument("--drivetrain", help="驱动方式")
    parser.add_argument("--image", help="图片URL")
    parser.add_argument("--version", help="新版本号")
    
    args = parser.parse_args()
    
    updater = CarDataUpdater()
    
    if args.action == "add":
        if not all([args.name, args.manufacturer, args.year, args.category, args.pi, args.drivetrain]):
            print("添加车型需要提供: name, manufacturer, year, category, pi, drivetrain")
            return
        
        car_data = {
            "name": args.name,
            "manufacturer": args.manufacturer,
            "year": args.year,
            "category": args.category.upper(),
            "pi": args.pi,
            "drivetrain": args.drivetrain.upper(),
            "imageUrl": args.image or f"/images/{args.manufacturer.lower()}-{args.name.lower().replace(' ', '-')}.jpg",
            "gameId": "fh5"
        }
        
        data = updater.add_car(car_data)
        updater.save_data(data)
        
    elif args.action == "remove":
        if not all([args.name, args.manufacturer, args.year]):
            print("删除车型需要提供: name, manufacturer, year")
            return
        
        data = updater.remove_car(args.name, args.manufacturer, args.year)
        updater.save_data(data)
        
    elif args.action == "update":
        if not all([args.name, args.manufacturer, args.year]):
            print("更新车型需要提供: name, manufacturer, year")
            return
        
        updates = {}
        if args.category:
            updates["category"] = args.category.upper()
        if args.pi:
            updates["pi"] = args.pi
        if args.drivetrain:
            updates["drivetrain"] = args.drivetrain.upper()
        if args.image:
            updates["imageUrl"] = args.image
        
        if not updates:
            print("请提供要更新的字段")
            return
        
        data = updater.update_car(args.name, args.manufacturer, args.year, updates)
        updater.save_data(data)
        
    elif args.action == "reload":
        updater.reload_server_data()
        
    elif args.action == "stats":
        updater.get_server_stats()

if __name__ == "__main__":
    main() 