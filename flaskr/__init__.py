from flask import Flask


def create_app():
    # create and configure the app
    app = Flask(__name__)

    # a simple page that says hello
    @app.route('/')
    def hello():
        return 'Hello, world!'

    return app
