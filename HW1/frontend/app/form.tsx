"use client";

import {
  Autocomplete,
  AutocompleteItem,
  Button,
  Input,
  Skeleton,
  Spinner,
} from "@nextui-org/react";
import { ChangeEvent, createContext, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { City } from "@/types/city";
import { getCities } from "@/service/cityService";
import { useCookies } from "next-client-cookies";
import { useRouter } from "next/navigation";

type a = {
  departure: number;
  setDeparture: (id: number) => void;
  arrival: number;
  seats: number;
  departureTime: string;
};

export const TripSearchContext = createContext<a>({
  departure: 0,
  setDeparture: () => {},
  arrival: 0,
  seats: 0,
  departureTime: "",
});

export default function Form() {
  const cookies = useCookies();
  const router = useRouter();

  const [departure, setDeparture] = useState<number>(0);
  const [arrival, setArrival] = useState<number>(0);
  const [seats, setSeats] = useState<number>(1);

  const { isPending: isCitiesPending, data: cities } = useQuery<City[]>({
    queryKey: ["cities"],
    queryFn: () => getCities(),
  });

  return (
    <div className="flex flex-col lg:flex-row gap-4 items-center justify-center">
      <Skeleton isLoaded={!isCitiesPending} className="rounded-lg">
        <Autocomplete
          label="Departure"
          id="departure"
          className="max-w-xs"
          onSelectionChange={(id) =>
            setDeparture(id ? parseInt(id.toString()) : 0)
          }
        >
          {cities
            ? cities.map((city: City) => (
                <AutocompleteItem
                  key={city.id}
                  value={city.name}
                  id={"departure" + city.id}
                >
                  {city.name}
                </AutocompleteItem>
              ))
            : []}
        </Autocomplete>
      </Skeleton>
      <Skeleton isLoaded={!isCitiesPending} className="rounded-lg">
        <Autocomplete
          label="Arrival"
          id="arrival"
          className="max-w-xs"
          onSelectionChange={(id) =>
            setArrival(id ? parseInt(id.toString()) : 0)
          }
        >
          {cities
            ? cities.map((city) => (
                <AutocompleteItem key={city.id} value={city.name} id={"arrival" + city.id}>
                  {city.name}
                </AutocompleteItem>
              ))
            : []}
        </Autocomplete>
      </Skeleton>
      <Input
        type="number"
        label="People"
        id="seats"
        min={1}
        defaultValue="1"
        className="lg:max-w-24"
        onChange={(event: ChangeEvent<HTMLInputElement>) =>
          setSeats(parseInt(event.target.value))
        }
      />
      <Button
        color="primary"
        type="submit"
        id="searchBtn"
        onClick={() => {
          cookies.set("departure", departure.toString());
          cookies.set("arrival", arrival.toString());
          cookies.set("seats", seats.toString());
          router.push("/trips");
        }}
      >
        Search
      </Button>
    </div>
  );
}
