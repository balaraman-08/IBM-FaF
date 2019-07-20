from flask import Flask


def create_app():
    # create and configure the app
    app = Flask(__name__)
    app.secret_key = 'willbereplacedbyarandomstring'

    # a simple page that says hello
    @app.route('/')
    def hello():
        return 'Hello, world!'

    from . import auth
    app.register_blueprint(auth.auth)

    from . import analyse
    app.register_blueprint(analyse.analyse)
    
    return app
