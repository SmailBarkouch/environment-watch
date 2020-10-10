#!/usr/bin/env python
from http.server import HTTPServer, BaseHTTPRequestHandler
from api import Handler

from io import BytesIO


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    def _set_response(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def do_GET(self):
        self._set_response()
        self.wfile.write(Handler().get_request(str(self.path)).format(self.wfile).encode('utf-8'))

    def do_POST(self):
        print(self.rfile.read())
        Handler().post_request(self.rfile.read())
        self._set_response()
        self.wfile.write("POST request for {}".format(self.path).encode('utf-8'))


httpd = HTTPServer(("0.0.0.0", 8000), SimpleHTTPRequestHandler)
httpd.serve_forever()
