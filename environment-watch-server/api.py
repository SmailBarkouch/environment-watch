class Handler:
    def get_request(self, path):
        if "/florida_jay_images" == path:
            return open("florida_jay/images.json", "r").read()
        elif "/red_cockaded_woodpecker_images" == path:
            return open("red_cockaded_woodpecker/images.json", "r").read()
        elif "/rusty_blackbird_images" == path:
            return open("rusty_blackbird/images.json", "r").read()
        elif "/yellow_billed_cuckoo_images" == path:
            return open("yellow_billed_cuckoo/images.json", "r").read()
        elif "/red_legged_kittiwake_images" == path:
            return open("red_legged_kittiwake/images.json", "r").read()
        elif "/florida_jay_coords" == path:
            return open("florida_jay/coords.txt", "r").read()
        elif "/red_cockaded_woodpecker_coords" == path:
            return open("red_cockaded_woodpecker/coords.txt", "r").read()
        elif "/rusty_blackbird_coords" == path:
            return open("rusty_blackbird/coords.txt", "r").read()
        elif "/yellow_billed_cuckoo_coords" == path:
            return open("yellow_billed_cuckoo/coords.txt", "r").read()
        elif "/red_legged_kittiwake_coords" == path:
            return open("red_legged_kittiwake/coords.txt", "r").read()
        elif "/all_coods" == path:
            self.get_all_coords()
        elif "/alerts" == path:
            self.get_alerts()
        return "NOO"


    def get_all_coords(self):
        return open("florida_jay/coords.txt", "r").read() + "\n" + \
               open("red_cockaded_woodpecker/coords.txt", "r").read() + "\n" + \
               open("rusty_blackbird/coords.txt", "r").read() + "\n" + \
               open("yellow_billed_cuckoo/coords.txt", "r").read() + "\n" + \
               open("red_legged_kittiwake/coords.txt", "r").read()

    def get_alerts(self, path):
        print(path)

    def post_request(self, path, body):
        if "/florida_jay_images" == path:
            with open("florida_jay/images.json", "a") as myfile:
                myfile.write(body)
        elif "/red_cockaded_woodpecker_images" == path:
            with open("red_cockaded_woodpecker/images.json", "a") as myfile:
                myfile.write(body)
        elif "/rusty_blackbird_images" == path:
            with open("rusty_blackbird/images.json", "a") as myfile:
                myfile.write(body)
        elif "/yellow_billed_cuckoo_images" == path:
            with open("yellow_billed_cuckoo/images.json", "a") as myfile:
                myfile.write(body)
        elif "/red_legged_kittiwake_images" == path:
            with open("red_legged_kittiwake/images.json", "a") as myfile:
                myfile.write(body)
        elif "/florida_jay_coords" == path:
            with open("florida_jay/coords.txt", "a") as myfile:
                myfile.write(body)
        elif "/red_cockaded_woodpecker_coords" == path:
            with open("red_cockaded_woodpecker/coords.txt", "a") as myfile:
                myfile.write(body)
        elif "/rusty_blackbird_coords" == path:
            with open("rusty_blackbird/coords.txt", "a") as myfile:
                myfile.write(body)
        elif "/yellow_billed_cuckoo_coords" == path:
            with open("yellow_billed_cuckoo/coords.txt", "a") as myfile:
                myfile.write(body)
        elif "/red_legged_kittiwake_coords" == path:
            with open("red_legged_kittiwake/coords.txt", "a") as myfile:
                myfile.write(body)

