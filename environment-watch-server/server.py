from http.server import HTTPServer, BaseHTTPRequestHandler
from api import Handler

from io import BytesIO


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        self.send_response(200)
        self.end_headers()
        self.wfile.write(Handler().get_request(str(self.path)).format(self.wfile).encode('utf-8'))

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        Handler().post_request(self.rfile.read(content_length))
        self.send_response(200)
        self.end_headers()
        self.wfile.write("okay")


httpd = HTTPServer(('10.0.0.207', 8000), SimpleHTTPRequestHandler)
httpd.serve_forever()
