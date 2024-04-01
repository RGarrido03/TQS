import { City, CityCreate } from "@/types/city";
import { BASE_API_URL } from "./config";

export const createCity = async (city: CityCreate): Promise<City> =>
  fetch(BASE_API_URL + "city", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(city),
  }).then((res) => res.json());

export const getCities = async (name?: string): Promise<City[]> =>
  fetch(
    BASE_API_URL +
      "city" +
      (typeof name !== "undefined" ? "?name=" + name : ""),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  ).then((res) => res.json());

export const getCity = async (id: number): Promise<City> =>
  fetch(BASE_API_URL + "city/" + id, {
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());

export const updateCity = async (id: number, city: CityCreate): Promise<City> =>
  fetch(BASE_API_URL + "city/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(city),
  }).then((res) => res.json());

export const deleteCity = async (id: number): Promise<any> =>
  fetch(BASE_API_URL + "city/" + id, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());
