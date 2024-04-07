"use client";

import { useQuery } from "@tanstack/react-query";
import { Trip } from "@/types/trip";
import { getTrip, getTrips } from "@/service/tripService";
import TripCard from "@/components/TripCard";
import { useCookies } from "next-client-cookies";
import { Button, Input } from "@nextui-org/react";
import { useState } from "react";
import { User, UserCreate } from "@/types/user";
import { createUser } from "@/service/userService";
import { createReservation } from "@/service/reservationService";
import { ReservationCreate } from "@/types/reservation";

export default function Trips() {
  const cookies = useCookies();
  const tripId = parseInt(cookies.get("trip") || "0");

  const trip = useQuery<Trip>({
    queryKey: ["trip", tripId],
    queryFn: () => getTrip(tripId),
  }).data;

  const [seats, setSeats] = useState<number>(
    parseInt(cookies.get("seats") || "1")
  );
  const [user, setUser] = useState<UserCreate>({
    username: "",
    name: "",
    email: "",
    password: "",
  });

  const submit = async () => {
    const UserCreation: User = await createUser(user);

    const reservation: ReservationCreate = {
      user: {
        id: UserCreation.id,
      },
      trip: {
        id: tripId,
      },
      seats,
    };

    await createReservation(reservation);
    alert("Reservation created successfully!");
  };

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Book your trip.
        </h1>
      </div>
      <div className="flex flex-col lg:flex-row px-4 md:px-8 lg:px-16 gap-8">
        <div className="flex flex-col gap-4 flex-2">
          <h2 className="text-2xl font-semibold text-balance">
            Fill your data
          </h2>
          <Input
            label="Username"
            onValueChange={(value: string) =>
              setUser((previous) => ({ ...previous, username: value }))
            }
          />
          <Input
            label="Name"
            onValueChange={(value: string) =>
              setUser((previous) => ({ ...previous, name: value }))
            }
          />
          <Input
            label="Email"
            type="email"
            onValueChange={(value: string) =>
              setUser((previous) => ({ ...previous, email: value }))
            }
          />
          <Input
            label="Password"
            type="password"
            onValueChange={(value: string) =>
              setUser((previous) => ({ ...previous, password: value }))
            }
          />
        </div>
        <div className="flex flex-col gap-4 flex-1">
          <h2 className="text-2xl font-semibold text-balance">Trip summary</h2>
          {trip && <TripCard trip={trip} clickable={false} />}
          <Input
            label="Seats"
            type="number"
            defaultValue={cookies.get("seats") || "1"}
            min={1}
            max={trip?.freeSeats}
            onValueChange={(value: string) => setSeats(parseInt(value))}
          />
          <Button color="primary" onClick={submit}>
            Submit
          </Button>
        </div>
      </div>
    </div>
  );
}
