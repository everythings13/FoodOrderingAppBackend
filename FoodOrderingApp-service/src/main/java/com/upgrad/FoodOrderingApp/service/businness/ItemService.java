package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDao itemDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RestaurantItemDao restaurantItemDao;

    @Autowired
    CategoryItemDao categoryItemDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    OrderDao orderDao;

    public List<Item> getItemsByPopularity(Restaurant restaurantEntity) {

        //Calls getOrdersByRestaurant method of orderDao to get the  OrdersEntity
        List<OrderEntity> ordersEntities = orderDao.getOrdersByRestaurant(restaurantEntity);

        //Creating list of ItemEntity which are ordered from the restaurant.
        List <ItemEntity> itemEntities = new LinkedList<>();

        //Looping in for each ordersEntity in ordersEntities to get the corresponding orders
        ordersEntities.forEach(ordersEntity -> {
            //Calls getItemsByOrders method of orderItemDao to get the  OrderItemEntity
            List <OrderItemEntity> orderItemEntities = orderItemDao.getItemsByOrders(ordersEntity);
            orderItemEntities.forEach(orderItemEntity -> { //Looping in to get each tem from the OrderItemEntity.
                itemEntities.add(orderItemEntity.getItem());
            });
        });

        //Creating a HashMap to count the frequency of the order.
        Map<String,Integer> itemCountMap = new HashMap<String,Integer>();
        itemEntities.forEach(itemEntity -> { //Looping in to count the frequency of Item ordered correspondingly updating the count.
            Integer count = itemCountMap.get(itemEntity.getUuid());
            itemCountMap.put(itemEntity.getUuid(),(count == null) ? 1 : count+1);
        });

        //Calls sortMapByValues method of uitilityProvider and get sorted map by value.
        Map<String,Integer> sortedItemCountMap = utilityProvider.sortMapByValues(itemCountMap);

        //Creating the top 5 Itementity list
        List<ItemEntity> sortedItemEntites = new LinkedList<>();
        Integer count = 0;
        for(Map.Entry<String,Integer> item:sortedItemCountMap.entrySet()){
            if(count < 5) {
                //Calls getItemByUUID to get the Itemtentity
                sortedItemEntites.add(itemDao.getItemByUUID(item.getKey()));
                count = count+1;
            }else{
                break;
            }
        }

        return sortedItemEntites;
    }
}