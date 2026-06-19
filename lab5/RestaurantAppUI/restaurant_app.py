import tkinter as tk
from functools import partial
from mq_communication import RabbitMq


class RestaurantApp:
    def __init__(self, gui):
        self.gui = gui
        self.gui.title("Restaurant App")
        self.gui.geometry("900x400")

        self.title_lbl = tk.Label(master=self.gui, text="Alege un tip de meniu (1-5):")
        self.title_lbl.grid(row=0, column=0, padx=10, pady=10)

        self.btn_menu_1 = tk.Button(master=self.gui, text="Comandă meniul 1",
                                    command=partial(self.send_request, request="order-1"))
        self.btn_menu_2 = tk.Button(master=self.gui, text="Comandă meniul 2",
                                    command=partial(self.send_request, request="order-2"))
        self.btn_menu_3 = tk.Button(master=self.gui, text="Comandă meniul 3",
                                    command=partial(self.send_request, request="order-3"))
        self.btn_menu_4 = tk.Button(master=self.gui, text="Comandă meniul 4",
                                    command=partial(self.send_request, request="order-4"))
        self.btn_menu_5 = tk.Button(master=self.gui, text="Comandă meniul 5",
                                    command=partial(self.send_request, request="order-5"))

        self.btn_menu_1.grid(row=1, column=0, padx=10, pady=5, sticky="ew")
        self.btn_menu_2.grid(row=2, column=0, padx=10, pady=5, sticky="ew")
        self.btn_menu_3.grid(row=3, column=0, padx=10, pady=5, sticky="ew")
        self.btn_menu_4.grid(row=4, column=0, padx=10, pady=5, sticky="ew")
        self.btn_menu_5.grid(row=5, column=0, padx=10, pady=5, sticky="ew")

        self.response_lbl = tk.Label(master=self.gui, text="Răspuns server:")
        self.response_lbl.grid(row=0, column=1, padx=10, pady=10)

        self.result = tk.Text(self.gui, width=70, height=20)
        self.result.grid(row=1, column=1, rowspan=6, padx=10, pady=10)

        self.rabbit_mq = RabbitMq(self)

    def send_request(self, request):
        self.rabbit_mq.send_message(message=request)
        # self.rabbit_mq.receive_message()

    def set_response(self, response_type, response):
        if response_type == "accepted":
            self.show_accepted(response)
        elif response_type == "error":
            self.show_error(response)
        elif response_type == "done":
            self.show_done(response)
        else:
            self.result.insert(tk.END, f"Mesaj necunoscut: {response_type}-{response}\n")

    def show_accepted(self, response):
        # aștept format: id~tipMeniu~preparationTime
        parts = response.split("-")
        if len(parts) >= 3:
            command_id = parts[0]
            menu_type = parts[1]
            prep_time = parts[2]

            self.result.insert(
                tk.END,
                f"[ACCEPTED]\n"
                f"ID comandă: {command_id}\n"
                f"Tip meniu: {menu_type}\n"
                f"Timp preparare: {prep_time}\n\n"
            )
        else:
            self.result.insert(tk.END, f"[ACCEPTED] {response}\n\n")

    def show_error(self, response):
        self.result.insert(tk.END, f"[ERROR] {response}\n\n")

    def show_done(self, response):
        self.result.insert(tk.END, f"[FINALIZATĂ] {response}\n\n")


if __name__ == "__main__":
    root = tk.Tk()
    app = RestaurantApp(root)
    root.mainloop()