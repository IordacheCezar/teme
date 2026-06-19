import pika
from retry import retry
import threading


class RabbitMq:
    config = {
        'host': 'localhost',
        'port': 5672,
        'username': 'student',
        'password': 'student',
        'exchange': 'restaurantapp.direct',
        'routing_key': 'restaurantapp.routingkey',
        'queue': 'restaurantapp.queue1'
    }

    credentials = pika.PlainCredentials(config['username'], config['password'])
    parameters = pika.ConnectionParameters(
        host=config['host'],
        port=config['port'],
        credentials=credentials
    )

    def __init__(self, ui):
        self.ui = ui
        threading.Thread(target=self.receive_message, daemon=True).start()

    def on_received_message(self, blocking_channel, deliver, properties, message):
        result = message.decode('utf-8')
        print("Mesaj primit din RabbitMQ:", result)

        try:
            response_type, response = result.split('-', 1)
            self.ui.set_response(response_type, response)
        except Exception as e:
            print(e)
            print("Format greșit al mesajului primit.")

    @retry(pika.exceptions.AMQPConnectionError, delay=5, jitter=(1, 3))
    def receive_message(self):
        with pika.BlockingConnection(self.parameters) as connection:
            with connection.channel() as channel:
                channel.basic_consume(
                    queue=self.config['queue'],
                    on_message_callback=self.on_received_message,
                    auto_ack=True
                )

                try:
                    channel.start_consuming()
                except pika.exceptions.ConnectionClosedByBroker:
                    print("Connection closed by broker.")
                except pika.exceptions.AMQPChannelError:
                    print("AMQP Channel Error")
                except KeyboardInterrupt:
                    print("Application closed.")

    def send_message(self, message):
        with pika.BlockingConnection(self.parameters) as connection:
            with connection.channel() as channel:
                channel.basic_publish(
                    exchange=self.config['exchange'],
                    routing_key=self.config['routing_key'],
                    body=message
                )