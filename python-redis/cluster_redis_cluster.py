from rediscluster import StrictRedisCluster


startup_nodes = [{"host": "127.0.0.1", "port": "7379"}]
redis = StrictRedisCluster(startup_nodes=startup_nodes, password='test', decode_responses=True)


if __name__ == '__main__':
    redis.set('key3', 'value3')
    print(redis.get('key3'))