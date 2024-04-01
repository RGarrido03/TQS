import { Bus, BusCreate } from "@/types/bus";
import { BASE_API_URL } from "./config";

export const createBus = async (bus: BusCreate): Promise<Bus> =>
  fetch(BASE_API_URL + "bus", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(bus),
  }).then((res) => res.json());

export const getBuses = async (): Promise<Bus[]> =>
  fetch(BASE_API_URL + "bus", {
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());

export const getBus = async (id: number): Promise<Bus> =>
  fetch(BASE_API_URL + "bus/" + id, {
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());

export const updateBus = async (id: number, bus: BusCreate): Promise<Bus> =>
  fetch(BASE_API_URL + "bus/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(bus),
  }).then((res) => res.json());

export const deleteBus = async (id: number): Promise<any> =>
  fetch(BASE_API_URL + "bus/" + id, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());
