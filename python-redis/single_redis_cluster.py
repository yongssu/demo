import redis

redis_conn1 = redis.Redis(host='127.0.0.1', port=6379, decode_responses=True)
pool = redis.ConnectionPool(
    host='127.0.0.1',
    port=6379,
    # password='test',
    db=0,
    max_connections=60,
    decode_responses=True,
    # connection options
    socket_timeout=60,
    socket_connect_timeout=60,
    socket_keepalive=False,
    socket_keepalive_options=None,
    retry_on_timeout=False,
)

redis_pool1 = redis.StrictRedis(connection_pool=pool)


def test_redis():
    # 命令行与redis原声命令一样
    redis_conn1.set('key1', 'value1')
    print(redis_conn1.get('key1'))


def test_pool():
    redis_pool1.set("key2", 'value2')
    print(redis_pool1.get("key2"))

if __name__ == '__main__':
    test_redis()
    test_pool()
